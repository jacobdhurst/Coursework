import argparse
import socket
import time
import bitstring
from aes import *
from Crypto.Util import number
from Crypto.PublicKey import RSA

# Handle command-line arguments
parser = argparse.ArgumentParser()
parser.add_argument("-ip", "--ipaddress", help='ip address where the server is running', required=True)
parser.add_argument("-p", "--port", help='port where the server is listening on', required=True)
parser.add_argument("-m", "--message", help='message to send to the server', required=True)
args = parser.parse_args()

# load server's public key
serverPublicKeyFileName = "serverPublicKey"
f = open(serverPublicKeyFileName,'r')
key = RSA.importKey(f.read())
n, e = key.n, key.e
MESSAGE_LENGTH = 15

b = 128
iterations = 0
bitsRecovered = list()

cipher = bytearray.fromhex('650892b97057f72a7f06d126bec319c38c4f1ee9e9284b3845e18387bee8440d20e4019fb14e0c8a7d140f7b3b367dce4016e8fbcbaff33a66e50fce9fce41b520af945a636d40dcb29d1d1a9a18ac55e9556ce2dc98f318772b8c5e4213490877bc8fff27ced7e80c6fccbca5a71215a392645041865db608a1388c2f5e0259')
c1 = number.bytes_to_long(cipher[:128])
c2 = number.long_to_bytes(c1, 128)
while True:
  print "[Iteration:", iterations, "]"
  print "[Bits recovered", bitsRecovered, "]"
  print "[Reformatting current AES guesses]"

  guess0 = list(0 for i in xrange(0, 128))
  guess1 = list(0 for i in xrange(0, 128))

  for i in range(0, len(bitsRecovered)):
    guess0[i] = bitsRecovered[i]
    guess1[i] = bitsRecovered[i]

  guess0.insert(0, 0)
  guess1.insert(0, 1)

  guess0 = guess0[:128]
  guess1 = guess1[:128]

  guess0 = bitstring.BitArray(bin=''.join(map(str, guess0))).tobytes()
  guess1 = bitstring.BitArray(bin=''.join(map(str, guess1))).tobytes()

  bit0Flag = False
  bit1Flag = False

  iterations = iterations + 1
  b = b - 1

  print "[Trying RSA Cipher:", number.bytes_to_long(cipher), "]"
  print "[Multiplying Cipher by 2^{be} mod n with b =", b, "]"
  c1 = number.bytes_to_long(cipher[:128])
  c1 = c1 * (2 ** (b * e) % n) % n
  c2 = number.long_to_bytes(c1, 128)
  print "[Resulting Cipher:", c1, "]"

  time.sleep(2)

  # Create a TCP/IP socket
  sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

  # Connect the socket to the port where the server is listening
  server_address = (args.ipaddress, int(args.port))
  sock.connect(server_address)

  # GUESS WITH 0 BIT
  try:
    # Send data
    message = str(args.message)

    aes = AESCipher(guess0)

    msg = ""
    msg += c2
    msg += aes.encrypt(message)

    print("|==============================MODIFIED MESSAGE SENDING THIS================================|")
    print '[Original:', message, "]"
    print '[Modified:', msg, "]"
    print "[AES:", aes.key, "]"
    print("|===========================================================================================|")

    # msg: AES key encrypted by the public key of RSA  + message encrypted by the AES key
    sock.sendall(msg) # msg

    # Look for the response
    amount_received = 0
    amount_expected = len(message)

    if amount_expected % 16 != 0:
      amount_expected += (16 - (len(message) % 16))

    answer = ""

    if amount_expected > amount_received:
      while amount_received < amount_expected:
        data = sock.recv(MESSAGE_LENGTH)
        amount_received += len(data)
        answer += data

      serverResponse = aes.decrypt(answer)

      print("|==========================================SERVER===========================================|")
      if serverResponse.__contains__("HELLOWORLD!123"):
        print "[Server:", serverResponse, "]"
        bit0Flag = True
      else:
        print "[Server failed]"
        bit0Flag = False
      print("|===========================================================================================|")

  finally:
    sock.close()

    # GUESS WITH 1 BIT
    time.sleep(2)

    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Connect the socket to the port where the server is listening
    server_address = (args.ipaddress, int(args.port))
    sock.connect(server_address)

    try:
      # Send data
      message = str(args.message)

      aes = AESCipher(guess1)

      msg = ""
      msg += c2
      msg += aes.encrypt(message)

      print("|==============================MODIFIED MESSAGE SENDING THIS================================|")
      print '[Original:', message, "]"
      print '[Modified:', msg, "]"
      print "[AES:", aes.key, "]"
      print("|===========================================================================================|")

      # msg: AES key encrypted by the public key of RSA  + message encrypted by the AES key
      sock.sendall(msg) # msg

      # Look for the response
      amount_received = 0
      amount_expected = len(message)

      if amount_expected % 16 != 0:
        amount_expected += (16 - (len(message) % 16))

      answer = ""

      if amount_expected > amount_received:
        while amount_received < amount_expected:
          data = sock.recv(MESSAGE_LENGTH)
          amount_received += len(data)
          answer += data

        serverResponse = aes.decrypt(answer)

        print("|==========================================SERVER===========================================|")
        if serverResponse.__contains__("HELLOWORLD!123"):
          print "[Server:", serverResponse, "]"
          bit1Flag = True
        else:
          print "[Server failed]"
          bit1Flag = False
        print("|===========================================================================================|")

    finally:
      sock.close()

    if bit0Flag:
      bitsRecovered.insert(0, 0)
      bit0Flag = False
    else:
      if bit1Flag:
        bitsRecovered.insert(0, 1)
        bit1Flag = False

    if iterations == 128:
      print "[Original AES key:", bitsRecovered, "]"
      bitsRecovered = bitsRecovered[:128]
      bitsRecovered = bitstring.BitArray(bin=''.join(map(str, bitsRecovered))).tobytes()

      msg = str(bytearray.fromhex('041c8a65a972cf6fd13ed3026a84a8600823e9cc0d574919d5bb7426647a3666b230533f46c4296f8e7004c2c0f6cf2796ea948b3cd6dcb33613c3c907ae408d'))

      aes = AESCipher(bitsRecovered)
      msg = aes.decrypt(msg)

      print "[Successfully decrypted message: ", msg, "]"
      break

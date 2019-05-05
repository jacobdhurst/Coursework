from statistics import mean

with open('results', 'r') as fd:
    line = fd.readline()
    i = 0
    pt_bd = []
    pt_da = []
    mem_bd = []
    mem_da = []
    while(line):
        line = fd.readline().split(' ')
        if(len(line) == 1): 
            mem_b = [x[0] for x in mem_bd]
            mem_d = [x[1] for x in mem_bd]
            mem_a = [x[1] for x in mem_da]

            pt_b = [x[0] for x in pt_bd]
            pt_d = [x[1] for x in pt_bd]
            pt_a = [x[1] for x in pt_da]

            print('Average free memory before vs during malloc: '+str(mean(mem_b))+' '+str(mean(mem_d)))
            print('Average page table size before vs during malloc: '+str(mean(pt_b))+' '+str(mean(pt_d)))
            print('Average free memory during malloc vs after access: '+str(mean(mem_d))+' '+str(mean(mem_a)))
            print('Average page table size during malloc vs after access: '+str(mean(pt_d))+' '+str(mean(pt_a)))
            exit(0)
        elif(len(line) == 8): 
            #print(line)
            continue
        line = list(filter(lambda x: x != '', line))
        #print(line)
        vf = int(line[4])
        vi = int(line[1])
        if i%4==0: 
            #print('mem before/during')
            mem_bd.append((vi,vf))
        elif i%4==1: 
            #print('pt before/during')
            pt_bd.append((vi,vf))
        elif i%4==2: 
            #print('mem during/after')
            mem_da.append((vi,vf))
        elif i%4==3: 
            #print('pt during/after')
            pt_da.append((vi,vf))
        i+=1
        #print(line)

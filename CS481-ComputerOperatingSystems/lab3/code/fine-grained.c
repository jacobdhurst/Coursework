#include <pthread.h>
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>

pthread_mutex_t mutex;
int access_count, insert_count;

struct LinkedList{
    int data;
    struct LinkedList *next;
 };

typedef struct LinkedList *node; //Define node as pointer of data type struct LinkedList

node head;

node createNode(){
    node temp; // declare a node
    temp = (node)malloc(sizeof(struct LinkedList)); // allocate memory using malloc()
    temp->next = NULL;// make next point to NULL
    return temp;//return the new node
}

node addNode(node head, int value){
    node temp,p;// declare two nodes temp and p
    temp = createNode();//createNode will return a new node with data = value and next pointing to NULL.
    temp->data = value; // add element's value to data part of node
    if(head == NULL){
        pthread_mutex_lock(&mutex);
        head = temp;     //when linked list is empty
    }
    else{
        p = head;//assign head to p 
        while(p->next != NULL){
            p = p->next;//traverse the list until p is the last node.The last node always points to NULL.
        }
        pthread_mutex_lock(&mutex);
        p->next = temp;//Point the previous last node to the new node created.
    }
    pthread_mutex_unlock(&mutex);
    return head;
}

node getNode(node head, int value){
    node p = head;//assign head to p 
    while(p->next != NULL){
        if(p->data == value)
        {
            return p;
        }
        p = p->next;//traverse the list until p is the last node.The last node always points to NULL.
    }
    return head;
}

void *inserting(void *args)
{
    while(1)
    {
        addNode(head, insert_count);
        insert_count++;
    }
}

void *accessing(void *args)
{
    while(1)
    {
        
        getNode(head, insert_count);
        access_count++;
    }
}

static void alarm_handle(int sig)
{
    printf("Threaded with fine-grained locking access count: %d.\n", access_count);
    printf("Threaded with fine-grained locking insert count: %d.\n", insert_count);
    exit(0);
}

int main(int argc, char *argv[]) 
{
    signal(SIGALRM, alarm_handle);
    alarm(atoi(argv[1]));

    head = createNode();
    if(pthread_mutex_init(&mutex, NULL) != 0) printf("Failed to initialize mutex.\n");
    pthread_t thread1, thread2;

    pthread_create(&thread1, NULL, &inserting, NULL);
    pthread_create(&thread2, NULL, &accessing, NULL);

    sleep(60);
    exit(0);
}
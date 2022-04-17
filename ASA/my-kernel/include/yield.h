#pragma once
#define MAGIC_CTX 0x1234
#define STACK_SIZE 8192 //donnee recup depuis stackoverflow
#define NULL ((void *)0)
#define CTX_INDFINI     0
#define CTX_START       1 //entrain de s'executer
#define CTX_READY       2 // vient d'etre créer
#define CTX_TERMINATED  3
#define CTX_WAITING     4 // ctx bloquer ou en attente

#define CTX_NUM_MAX 16
typedef void (*func_t)(void *);


struct ctx_s {
    int magic;
    void *sp;
    void *bp;
    unsigned char pile[STACK_SIZE];
    func_t pf;
    void *args;
    int status;
    struct ctx_s *next;
    struct ctx_s *prev;
};
void init_ctx();
struct ctx_s * create_ctx(func_t pf, void *args);
void free_ctx(struct ctx_s *ctx);
void switch_to_ctx(struct ctx_s *c);
void yield();

#define READ_CTX(bp,sp) {        \
    asm( "mov %%ebp, %0" "\n\t"  \
         "mov %%esp, %1" "\n\t"  \
         : "=r"(bp),             \
           "=r"(sp)); } 

#define WRITE_CTX(bp, sp) {      \
    asm( "mov %0, %%ebp" "\n\t"  \
         "mov %1, %%esp" "\n\t"  \
         : \
         : "r"(bp),              \
           "r"(sp)); }

#include "ioport.h"
#include "gdt.h"
#include "idt.h"
#include "miniio.h"
#include "yield.h"
#include "semaphore.h"

struct ctx_s *ctx_ping = NULL;
struct ctx_s *ctx_pong = NULL;


sem_t sem_cpt;
sem_t sem_clavier;

sem_t sem_ping;
sem_t sem_pong;

void empty_irq(int_regs_t *r) {
}

void yield_irq(int_regs_t *r) {
  yield();
}


void cpt_disp(void *arg) {
  while(1) {
    display_cpt();
    sem_up(&sem_clavier);
    sem_down(&sem_cpt);
  }
}
void sem_clavier_print(void *arg) {
  while(1) {
    controleur_clavier();
    sem_up(&sem_cpt);
    sem_down(&sem_clavier);
  }
}

void ping(void *arg) {
  while (1) {
    puts("piiing\n");
    for(int i=0;i<10000000;i++);
    //sem_up(&sem_pong);
    //sem_down(&sem_ping);
    // putc('1');
    // yield();
    // //switch_to_ctx(ctx_pong);
    // putc('2');
    //yield();
    // //switch_to_ctx(ctx_pong);
  }
}

void pong(void *arg) {
  while (1) {
    puts("pooong \n");
    for(int i=0;i<10000000;i++);
    //sem_up(&sem_ping);
    //sem_down(&sem_pong);
    // putc('A');
    // yield();
    // //switch_to_ctx(ctx_ping);
    // putc('B');
    //yield();
    // //switch_to_ctx(ctx_ping);
  }
}

/* multiboot entry-point with datastructure as arg. */
void main(unsigned int * mboot_info)
{
    /* clear the screen */
    clear_screen();
    puts("Early boot.\n");
    puts("\t-> Setting up the GDT... ");
    gdt_init_default();
    puts("OK\n");

    puts("\t-> Setting up the IDT... ");
    setup_idt();
    puts("OK\n");

    puts("\n\n");
    // ------------ PARTIE 1 ----------------------------

    print_Hello_World();

    idt_setup_handler(0, empty_irq);
    idt_setup_handler(1,controleur_clavier);
// --------------------------------------------------------

// ----------- Partie 2  yield -----------------------------------
    // init_ctx();
    // create_ctx(ping, 0);
    // create_ctx(pong, 0);
    // yield();

// Un premier démonstrateur avec les sémaphores
    init_ctx();
    init_semaphore(&sem_cpt,0);
    init_semaphore(&sem_clavier,0);
    //
    //
    // init_semaphore(&sem_ping,0);
    // init_semaphore(&sem_pong,0);
    // create_ctx(ping, 0);
    // create_ctx(pong, 0);
    create_ctx(cpt_disp, 0);
    create_ctx(sem_clavier_print, 0);
    idt_setup_handler(0,yield_irq);
// ------------------------------------------------------------------

    __asm volatile("sti");
    /* minimal setup done ! */


    puts("Going idle\n");

    for(;;) {} /* nothing more to do... really nothing ! */

}

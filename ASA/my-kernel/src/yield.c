#include "yield.h"
#include "stmanip.h"
#include "ioport.h"
#include "list_contexts.h"
#include "semaphore.h"

struct ctx_s *current; //contexte courant
struct ctx_s *head; //premier contexte disponible
struct ctx_s ctxs[CTX_NUM_MAX]; //au lieu du malloc


static liste_ctxs list; // liste des contextes disponibles

//ici on initialise tous les contextes (tableau)
void init_ctx() {
  init_liste_ctxs(&list);
  for (int i =0; i < CTX_NUM_MAX; i++) {
    ctxs[i].status = CTX_INDFINI;
    ctxs[i].next = &ctxs[i+1];
    ctxs[i].prev = NULL;
    ctxs[i].magic = MAGIC_CTX;

  }
  ctxs[CTX_NUM_MAX-1].next = NULL;
  head = &ctxs[0];
  current = NULL;


}

struct ctx_s * create_ctx(func_t pf, void *args) {
  struct ctx_s *ctx;
  if (head == NULL) return NULL;
  ctx = head;
  head = head->next;

  ctx->sp = ctx->pile + STACK_SIZE;
  ctx->bp = ctx->pile + STACK_SIZE;;
  ctx->pf = pf;
  ctx->args = args;
  ctx->status = CTX_START;
  ctx->magic = MAGIC_CTX;
  //pour la partie sémaphores
  insert_into_list(&list, ctx);//on l'ajoute à la liste des contexte libre
  return ctx;
}

void free_ctx(struct ctx_s *ctx) {
  ctx->status = CTX_INDFINI;
  remove_liste_ctxs(&list, ctx);
  ctx->next = head;
  head = ctx;

}

void switch_to_ctx(struct ctx_s *c) {
  __asm volatile("cli");
  if (current != NULL) {
    /* sauvergarder les pointeurs de pile dans le contexte courant */
    READ_CTX(current->bp,current->sp);
  }
  current = c;
  /* restaurer le contexte passé en paramètre */
  WRITE_CTX(current->bp, current->sp);

  switch (current->status) {
    case CTX_START:
          current->status = CTX_READY;
          //envoyer le code au controleur d'interruption avant de changer de ctx
          //interruption
          _outb(0xA0, 0x20);
          _outb(0x20, 0x20);
          current->pf(current->args);
          current->status = CTX_TERMINATED;
          yield();
    default:
          return;
    __asm volatile("sti");


  }
}
void yield(){
  struct ctx_s *tmp1;
  struct ctx_s *tmp2;

  tmp1 = next_ctx(&list, current);
  while ((tmp1->status== CTX_TERMINATED) || (tmp1->status == CTX_WAITING) || (tmp1->status == CTX_INDFINI)) {
    if(tmp1->status == CTX_TERMINATED) {
      tmp2 = tmp1;
      tmp1 = next_ctx(&list, tmp2);
      free_ctx(tmp2);
    }
    else {
      tmp1 = next_ctx(&list, tmp1);
    }
  }
  switch_to_ctx(tmp1);
}


void init_semaphore(sem_t *s, int cpt) {
  s->cpt = cpt;
}

void sem_down(sem_t *s) {
  __asm volatile("cli");
  s->cpt--;
  if ((s->cpt < 0)) {
    // bloquer le contexte courrant
      current->status = CTX_WAITING;
      // supprimer le contexte courant de la liste des ctx libres
      remove_liste_ctxs(&list, current);
      //inserer le ctx courrant à la liste des ctx bloquer
      insert_into_list(&s->liste_ctx_blocked, current);
      // on appel l'ordonnanceur
      yield();
  }

  __asm volatile("sti");
}
void sem_up(sem_t *s) {
  __asm volatile("cli");
  s->cpt++;
  if (s->cpt <= 0) {
    struct ctx_s *st = next_ctx(&s->liste_ctx_blocked, NULL);
    //debloquer le ctx
    st->status = CTX_READY;

    //le supprimer de la liste des ctx bloquer
    remove_liste_ctxs(&s->liste_ctx_blocked, st);
    //l'ajouter à la liste des ctx libre
    insert_into_list(&list, st);
    // on appel l'ordonnanceur

  //  yield();
}
  __asm volatile("sti");


}

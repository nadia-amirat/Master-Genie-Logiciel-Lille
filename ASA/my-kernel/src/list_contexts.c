#include "list_contexts.h"


/*
Cette fonction permet d'initialiser la liste de tous les contextes
à une liste vide.
*/
void init_liste_ctxs(liste_ctxs *l) {
  l->next = l;
  l->prev = l;
}

/*
Cette fonction permet d'inserer dans une liste de contextes donné un contexte
l'insertion se fait en début de liste.
*/
void insert_into_list(liste_ctxs *l, struct ctx_s *ctx) {
  ctx->next= l->next;
  l->next->prev = ctx;
  ctx->prev = l;
  l->next = ctx;
}

/*
Cette fonction permet de supprimer un contexte d'une liste de contextes.
*/
struct ctx_s * remove_liste_ctxs(liste_ctxs *l, struct ctx_s *ctx) {
  if (isEmpty_liste_ctxs(l)) return NULL;
  struct ctx_s *p = l->next;
  while (p != l) {
    if (p == ctx) break;
    p = p->next;
  }
  if (p == l)
    return  NULL;
  p->prev->next = p->next;
  p->next->prev = p->prev;
  return p;

}
int isEmpty_liste_ctxs(liste_ctxs *l) {
  return (l->next == l);
}

struct ctx_s *next_ctx(liste_ctxs *l, struct ctx_s *current) {
  if (current == NULL) return l->next;
  else if (current->next != l) return current->next;
  else return current->next->next;
}

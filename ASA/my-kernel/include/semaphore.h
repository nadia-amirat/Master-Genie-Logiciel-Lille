#pragma once

#include "list_contexts.h"


typedef struct sem_s {
  liste_ctxs liste_ctx_blocked; //ctxs  en liste d'attente
  int cpt;
} sem_t;

void init_semaphore(sem_t *s, int cpt);
void sem_down(sem_t *s);
void sem_up(sem_t *s);

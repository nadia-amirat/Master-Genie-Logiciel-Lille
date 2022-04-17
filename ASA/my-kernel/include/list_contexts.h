//signifie que le fichier courant doit etre inclut une seule fois dans la compilation
#pragma once

#include "yield.h"


typedef struct ctx_s liste_ctxs;


void init_liste_ctxs(liste_ctxs *l);
void insert_into_list(liste_ctxs *l, struct ctx_s *ctx);
struct ctx_s * remove_liste_ctxs(liste_ctxs *l, struct ctx_s *ctx);
int isEmpty_liste_ctxs(liste_ctxs *l);
struct ctx_s *next_ctx(liste_ctxs *l, struct ctx_s *current);

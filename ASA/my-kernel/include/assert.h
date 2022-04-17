#ifndef _ASSERT_H_
#define _ASSERT_H_

#include "miniio.h"

#define assert(TEST) do {                \
    if(!(TEST)) {                        \
        puts(__FILE__ ":");              \
        putdec(__LINE__);                \
        puts(": assert failed: " #TEST); \
        for(;;);                         \
    } } while(0)

#endif

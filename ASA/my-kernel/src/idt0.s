%macro ISR_NOERRCODE 1
global isr%1
isr%1:
    cli
    push byte 0
    push byte %1
    jmp process_isr
%endmacro

%macro ISR_ERRCODE 1
global isr%1
isr%1:
    cli
    push byte %1
    jmp process_isr
%endmacro

%macro IRQ 2
global irq%1
irq%1:
    cli
    push byte 0
    push byte %2
    jmp process_irq
%endmacro

%macro DEFINE_HANDLER 1
extern %1_handler

process_%1:
    pusha
    push esp
    mov si, ds
    mov ax, 0x10
    mov ds, ax
    mov es, ax
    mov fs, ax
    mov gs, ax
   
    mov eax, esp
    push eax
    call %1_handler
    pop eax
    add esp, 4
    mov ds, si
    mov es, si
    mov fs, si
    mov gs, si
    popa
    add esp, 8
    iret
%endmacro

DEFINE_HANDLER isr
DEFINE_HANDLER irq

global zero_idt
zero_idt:
    mov edi, [esp + 4] ; Set begin to IDT pointer
    mov eax, 0x00000000 ; Zero this
    mov ecx, 0x200 ; 256 entries, 2 words per entry : 512
    rep stosd ; So we would write 512 null words - this should zero our IDT
    ret

ISR_NOERRCODE 0
ISR_NOERRCODE 1
ISR_NOERRCODE 2
ISR_NOERRCODE 3
ISR_NOERRCODE 4
ISR_NOERRCODE 5
ISR_NOERRCODE 6
ISR_NOERRCODE 7
ISR_ERRCODE   8
ISR_NOERRCODE 9
ISR_ERRCODE   10
ISR_ERRCODE   11
ISR_ERRCODE   12
ISR_ERRCODE   13
ISR_ERRCODE   14
ISR_NOERRCODE 15
ISR_NOERRCODE 16
ISR_NOERRCODE 17
ISR_NOERRCODE 18
ISR_NOERRCODE 19
ISR_NOERRCODE 20
ISR_NOERRCODE 21
ISR_NOERRCODE 22
ISR_NOERRCODE 23
ISR_NOERRCODE 24
ISR_NOERRCODE 25
ISR_NOERRCODE 26
ISR_NOERRCODE 27
ISR_NOERRCODE 28
ISR_NOERRCODE 29
ISR_NOERRCODE 30
ISR_NOERRCODE 31
IRQ 0 , 32
IRQ 1 , 33
IRQ 2 , 34
IRQ 3 , 35
IRQ 4 , 36
IRQ 5 , 37
IRQ 6 , 38
IRQ 7 , 39
IRQ 8 , 40
IRQ 9 , 41
IRQ 10 , 42
IRQ 11 , 43
IRQ 12 , 44
IRQ 13 , 45
IRQ 14 , 46
IRQ 15 , 47

; We only handle the processor's and PIC's interrupts right now.
; If you want to handle other software interrupts, feel free to add something like
; ISR_NOERRCODE XX 
; XX being the interrupt number.
; Here is an example for the 87 interrupt :
ISR_NOERRCODE 87

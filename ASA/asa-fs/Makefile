HWDIR=hw

CC     = gcc
CFLAGS = -Wall -Wextra
CFLAGS += -g

LIBDIR = $(HWDIR)/lib
INCDIR = -I$(HWDIR)/include
LIBS   = -L$(LIBDIR) -lhardware

BINARIES := mkhd dump frmt mkvol dvol test_drive 

all: $(BINARIES)

mkhd: mkhd.o
	$(CC) $(CFLAGS) -o mkhd mkhd.o $(LIBS)

mkhd.o: mkhd.c
	$(CC) $(CFLAGS) -c mkhd.c $(INCDIR)

dump: dump.o
	$(CC) $(CFLAGS) -o dump dump.o $(LIBS)

dump.o: dump.c
	$(CC) $(CFLAGS) -c dump.c $(INCDIR)

frmt: frmt.o
	$(CC) $(CFLAGS) -o frmt frmt.o $(LIBS)

frmt.o: frmt.c
	$(CC) $(CFLAGS) -c frmt.c $(INCDIR)

drive.o: drive.c
	$(CC) $(CFLAGS) -c drive.c $(INCDIR)


mbr.o: mbr.c drive.h
	$(CC) $(CFLAGS) -c mbr.c $(INCDIR)


dvol: dvol.o drive.o mbr.o
	$(CC) $(CFLAGS) -o dvol dvol.o drive.o mbr.o $(LIBS)

dvol.o: dvol.c mbr.h
	$(CC) $(CFLAGS) -c dvol.c $(INCDIR)


mkvol: mkvol.o drive.o  mbr.o
	$(CC) $(CFLAGS) -o mkvol mkvol.o drive.o  mbr.o $(LIBS)

mkvol.o: mkvol.c drive.h mbr.h
	$(CC) $(CFLAGS) -c mkvol.c $(INCDIR)

test_drive: test_drive.o drive.o
	$(CC) $(CFLAGS) -o test_drive test_drive.o drive.o $(LIBS)

test_drive.o: test_drive.c
	$(CC) $(CFLAGS) -c test_drive.c $(INCDIR)



clean:
	$(RM) *.o $(BINARIES) vdisk?.bin

.PHONY: all clean

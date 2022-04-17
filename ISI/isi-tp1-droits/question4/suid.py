import os
 

euid = os.geteuid()
egid = os.getegid()
print("Effective user ID is :", euid)

print("Effective Group ID is :", egid)

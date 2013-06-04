__author__ = 'heeseo'

import json, socket, sys

HOST, PORT = "localhost", 9999
names = ['hee','haewon','dongju']

# Test JSON locally ( dumps and loads )
# Turple -> (Dump) -> JSON -> (Load) -> Turple
send_json_data = json.dumps(names)
output_name = json.loads(send_json_data)

data = raw_input("Input your string: ")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    sock.connect((HOST,PORT))
    sock.sendall(send_json_data)
    received = sock.recv(1024)
finally:
    sock.close()

print "Sent:    {}".format(data)
print "Received: {}".format(received)



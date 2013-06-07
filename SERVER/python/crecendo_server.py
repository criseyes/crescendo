__author__ = 'heeseo'

import SocketServer
import socket
import json, sqlite3, os
import sys

def print_function():
    print 'test'
    return 0

#  TCPServer is able to service requests from clients asynchronously
class CrecendoTCPHandler(SocketServer.BaseRequestHandler):

    handler_tbl = {}

    def __init__(self, info):
        self.HOST, self.PORT = info
        self.initialize_handler('MsgInfo.java')

    def initialize_db(self,db_name):


    def initialize_handler(self, file_name):
        with open(file_name,'r') as f:
            for line in f:
                if 'msgDir' in line:
                    print 'end'
                    break
                elif 'final static int' in line:
                    print line.strip().split()[3].lower()
                    self.handler_tbl[line.strip().split()[5].split(';')[0]] = 'handle_'+(line.strip().split()[3].lower())

        print self.handler_tbl
        f.close()

    def handle_register_user(self):
        return print_function

    def handle_sys_login(self):
        return print_function

    def handle_add_new_plan(self):
        return print_function

    def handle_update_plan(self):
        return print_function

    def handle_del_plan(self):
        return print_function

    def handle_get_plan_cnt(self):
        return print_function

    def handle_get_plan(self):
        return print_function

    def handle_get_friend_cnt(self):
        return print_function

    def handle_get_friend(self):
        return print_function

    def handle_add_friend(self):
        return print_function

    def handle_del_friend(self):
        return print_function

    def handle_sel_avata_friend(self):
        return print_function


    def handle(self):
        self.data = self.request.recv(1024).strip()
        print "{} wrote:".format(self.client_address[0])
        print self.data

        # Read JSON for the first part as message header
        msghead = json.loads(self.data)
        self.hander_tbl[msghead[0]](msghead[]);

        # Access DB sqlite

        #db_conn = sqlite3.connect("test.db")
        #db_cur = db_conn.cursor()
        #db_cur.execute("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'user' ")
        #if not db_cur.rowcount:
        #    db_cur.execute(".....")

        # Reply result data to the client
        self.request.sendall(self.data.upper())

    def run(self):
        #HOST, PORT = "localhost", 9999
        print self.HOST, self.PORT
        server = SocketServer.TCPServer((self.HOST,self.PORT), CrecendoTCPHandler)

        server.serve_forever()

    def stop(self):
        SocketServer.TCPServer.allow_reuse_address = True



if __name__ == "__main__":
    sv = CrecendoTCPHandler(["localhost",9999])
    sv.run()

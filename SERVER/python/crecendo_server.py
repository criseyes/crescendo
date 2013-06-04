__author__ = 'heeseo'

import SocketServer
import socket
import json, sqlite3, os

#  TCPServer is able to service requests from clients asynchronously

class MyTCPHandler(SocketServer.BaseRequestHandler):

    def handle(self):
        self.data = self.request.recv(1024).strip()
        print "{} wrote:".format(self.client_address[0])

        # Test JSON
        print json.loads(self.data)

        # Access DB sqlite

        #db_conn = sqlite3.connect("test.db")
        #db_cur = db_conn.cursor()
        #db_cur.execute("SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'user' ")
        #if not db_cur.rowcount:
        #    db_cur.execute(".....")

        # Reply result data to the client

        #self.request.sendall(self.data.upper())

if __name__ == "__main__":
    HOST, PORT = "localhost", 9999

    server = SocketServer.TCPServer((HOST,PORT), MyTCPHandler)
    server.serve_forever()

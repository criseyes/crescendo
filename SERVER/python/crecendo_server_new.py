__author__ = 'heeseo'

import logging
import sys
import SocketServer
import json, MySQLdb 

logging.basicConfig(level=logging.DEBUG,
                    format='%(name)s: %(message)s',
                    )

handler_tbl = {}
msg_type_file = '../../CLIENT/src/com/kaist/crescendo/manager/MsgInfo.java'
msg_header = {"msgId":0,"msgLen":0,"msgDir":0,"msgRet":0,"msgBody":{}} 

class CrecendoRequestHandler(SocketServer.BaseRequestHandler):

    def __init__(self, request, client_address, server):
        self.logger = logging.getLogger('CrecendoRequestHandler')
        self.logger.debug('__init__')

        SocketServer.BaseRequestHandler.__init__(self, request, client_address, server)
        return

    def setup(self):
        self.logger.debug('setup')
        return SocketServer.BaseRequestHandler.setup(self)

    def handle(self):
        self.logger.debug('handle')
	retVal = 0
        # Echo the back to the client
        data = self.request.recv(1024)
	print "================================================================"
	print data 
	print "================================================================"
	# Parsing the JSON data to extract message's header
        msgData = json.loads(data)
	msgCmd = msgData['msgId']

	# Call hander function based on msgId 
	retVal = getattr(self,handler_tbl[msgCmd])(json.loads(data)['msgBody'])

	# Create Message data to reply to client 
	msgData["msgDir"] = 0
	msgData["msgRet"] = retVal
	
	reply_data = json.dumps(msgData)
       	
	self.logger.debug('recv()->"%s"', reply_data)
        self.request.send(reply_data)
        return

    def handle_register_user(self, msgBody):
        print 'haha handle_register_user'
	print msgBody 
	print type(msgBody)
	query_string = "insert into user_table (userid, birthday, phoneno, passwd) values ('%s', '%s', '%s', '%s');"%(\
		msgBody["userId"],msgBody["birthDay"],msgBody["phoneNum"],msgBody["passWord"])	
	print query_string
 
	db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
	cur = db.cursor()
	return cur.execute(query_string)

    def finish(self):
        self.logger.debug('finish')
        return SocketServer.BaseRequestHandler.finish(self)

class CrecendoServer(SocketServer.TCPServer):

    def __init__(self, server_address, handler_class=CrecendoRequestHandler):
        self.logger = logging.getLogger('CrecendoServer')
        self.logger.debug('__init__')

        self.initialize_handler()
        self.initialize_db()
        SocketServer.TCPServer.__init__(self, server_address, handler_class)
        return

    def initialize_handler(self):
        with open(msg_type_file,'r') as f:
            for line in f:
                if 'msgDir' in line:
                    print 'end'
                    break
                elif 'final static int' in line:
                    handler_tbl[int(line.strip().split()[5].split(';')[0],16)] = 'handle_'+(line.strip().split()[3].lower())

        f.close()

    def initialize_db(self):
        print 'initdb'

    def server_activate(self):
        self.logger.debug('server_activate')
        SocketServer.TCPServer.server_activate(self)
        return

    def serve_forever(self):
        self.logger.debug('waiting for request')
        self.logger.info('Handling requests, press <Ctrl-C> to quit')
        while True:
            self.handle_request()
        return

    def handle_request(self):
        self.logger.debug('handle_request')
        return SocketServer.TCPServer.handle_request(self)

    def verify_request(self, request, client_address):
        self.logger.debug('verify_request(%s, %s)', request, client_address)
        return SocketServer.TCPServer.verify_request(self, request, client_address)

    def process_request(self, request, client_address):
        self.logger.debug('process_request(%s, %s)', request, client_address)
        return SocketServer.TCPServer.process_request(self, request, client_address)

    def server_close(self):
        self.logger.debug('server_close')
        return SocketServer.TCPServer.server_close(self)

    def finish_request(self, request, client_address):
        self.logger.debug('finish_request(%s, %s)', request, client_address)
        return SocketServer.TCPServer.finish_request(self, request, client_address)

    def close_request(self, request_address):
        self.logger.debug('close_request(%s)', request_address)
        return SocketServer.TCPServer.close_request(self, request_address)

if __name__ == '__main__':
    import socket
    import threading

    address = ('172.27.175.37', 9999) # let the kernel give us a port
    server = CrecendoServer(address, CrecendoRequestHandler)
    ip, port = server.server_address # find out what port we were given

    t = threading.Thread(target=server.serve_forever)
    t.setDaemon(True) # don't hang on exit
    t.start()

    logger = logging.getLogger('client')
    logger.info('Server on %s:%s', ip, port)

    server.serve_forever()

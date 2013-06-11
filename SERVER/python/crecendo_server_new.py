__author__ = 'heeseo'

import logging
import sys
import SocketServer
import json, MySQLdb, datetime  

logging.basicConfig(level=logging.DEBUG,
                    format='%(name)s: %(message)s',
                    )

handler_tbl = {}
mysql_err_tbl = {1062:0x0001}
msg_type_file = '../../CLIENT/src/com/kaist/crescendo/manager/MsgInfo.java'
msg_header = {"msgId":0,"msgLen":0,"msgDir":0,"msgRet":0,"msgBody":{}} 
plan_header = ["defUserId","planType","planInitVal", "planStartDate", \
		"planIsSelected", "planTitle", "planTargetVal", "planEndDate",\
		"planDayofWeek", "planUId", "planHistory"] 

 

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

	try: 
		# Call hander function based on msgId 
		(retVal, ret_msgBody) = getattr(self,handler_tbl[msgCmd])(json.loads(data)['msgBody'])
	except KeyError:
		msgData['msgBody'] = { "defUserId": 'zziniart@gmail.com' } 
		(retVal, ret_msgBody) = getattr(self,handler_tbl[msgCmd])(msgData['msgBody'])


	# Create Message data to reply to client 
	msgData["msgDir"] = 0
	msgData["msgRet"] = retVal
	msgData["msgBody"] = ret_msgBody 

	reply_data = json.dumps(msgData)
       	
	self.logger.debug('recv()->"%s"', reply_data)
        self.request.send(reply_data)
	

        return

    def handle_add_new_plan(self, msgBody):
	print 'haha handle_add_new_plan'
	print msgBody

	db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
	cur = db.cursor()
	retval = cur.execute("select * from user_plan_table where userid = '%s';"%(\
                msgBody["defUserId"].strip()))

	if retval != 0:  
		query_string = "insert into user_plan_table (userid, planType, planInitVal, planStartDate, planIsSelected, planTitle, planTargetVal,planEndDate, planDayofWeek) values ('%s', %s, %s, '%s', %s, '%s', %s, '%s', %s );"%(\
			msgBody[plan_header[0]].strip(),str(msgBody[plan_header[1]]).strip(),str(msgBody[plan_header[2]]).strip(),\
			msgBody[plan_header[3]].strip(),str(msgBody[plan_header[4]]).strip(),msgBody[plan_header[5]].strip(),\
			str(msgBody[plan_header[6]]).strip(),msgBody[plan_header[7]].strip(),str(msgBody[plan_header[8]]).strip())	
		print query_string
	else:
		query_string = "insert into user_plan_table (userid, planType, planInitVal, planStartDate, planIsSelected, planTitle, planTargetVal,planEndDate, planDayofWeek) values ('%s', %s, %s, '%s', %s, '%s', %s, '%s', %s );"%(\
			msgBody[plan_header[0]].strip(),str(msgBody[plan_header[1]]).strip(),str(msgBody[plan_header[2]]).strip(),\
			msgBody[plan_header[3]].strip(),'1',msgBody[plan_header[5]].strip(),\
			str(msgBody[plan_header[6]]).strip(),msgBody[plan_header[7]].strip(),str(msgBody[plan_header[8]]).strip())	
		print query_string
 
	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
		print cur.fetchone()
		return (0,msgBody) 
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])
		return (int(mysql_err_tbl[e.args[0]]),msgBody)

    def handle_update_plan(self,msgBody):
	print "handl_update_plan"
	query_string = "update user_plan_table set planType=%s, planInitVal=%s, planStartDate='%s', planIsSelected =%s, planTitle='%s', planTargetVal=%s, planEndDate='%s', planDayofWeek=%s where plandId=%s;"%(\
		str(msgBody[plan_header[1]]).strip(),str(msgBody[plan_header[2]]).strip(),\
		msgBody[plan_header[3]].strip(),str(msgBody[plan_header[4]]).strip(),msgBody[plan_header[5]].strip(),\
		str(msgBody[plan_header[6]]).strip(),msgBody[plan_header[7]].strip(),str(msgBody[plan_header[8]]).strip(),\
		str(msgBody[plan_header[9]]))	
	print query_string

	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
		return (0,msgBody) 
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])
		return (int(mysql_err_tbl[e.args[0]]),msgBody)


    def handle_del_plan(self,msgBody):
	print "handl_del_plan"
	query_string = "delete from user_plan_table where plandId=%s;"%(\
		msgBody["planUId"].strip())	
	print query_string

	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
		print cur.fetchone()
		return (0,msgBody) 
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])
		return (int(mysql_err_tbl[e.args[0]]),msgBody)

    def handle_get_plan(self,msgBody):

	query_string = "select * from user_plan_table where userid = '%s';"%(\
		msgBody["defUserId"].strip())	
	print query_string

	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
	
		if retval == 0:
			return (0, [])	
		idx = 0
		plan_row = { }
		receive_msgBody = [] 
		print type(idx)
		for rec in cur.fetchall():
			print "-------------------------"
			print rec 
			for row in rec:
				if type(row) is datetime.date: 
					plan_row[plan_header[idx]]  = str(row)
				else:
					plan_row[plan_header[idx]]  = row
				print type(row)
				idx += 1  
			plan_row["planHistory"] = []
			idx = 0
			print plan_row
			print '==================================='
			receive_msgBody.append(plan_row) 
			plan_row = {} 
			print receive_msgBody 
		return (0, receive_msgBody)
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])

	return (0,{}) # no plan  

    def handle_register_user(self, msgBody):
        print 'haha handle_register_user'
	print msgBody 
	print type(msgBody)
	query_string = "insert into user_table (userid, birthday, phoneno, passwd) values ('%s', '%s', '%s', '%s');"%(\
		msgBody["userId"].strip(),msgBody["birthDay"].strip(),msgBody["phoneNum"].strip(),msgBody["passWord"].strip())	
	print query_string
 
	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
		print cur.fetchone()
		return (0, msgBody) 
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])
		return (int(mysql_err_tbl[e.args[0]]), msgBody)

	
    def handle_sys_login(self, msgBody):
	print 'haha sys login'
	print msgBody

	query_string = "select passwd from user_table where userid = '%s';"%(\
		msgBody["userId"].strip())	
	print query_string

	try:
		db = MySQLdb.connect(db='crecendo', user='root', passwd='test1234')
		cur = db.cursor()
		retval = cur.execute(query_string)
		row = cur.fetchone()
	except MySQLdb.Error, e:
		print "MySQL Error [%d]: %s" % (e.args[0], e.args[1])

	if row == None:
		print 'unregister user'
		return (0x0002,msgBody) # unregister user 
	
	print type(str(row[0]))
	print row[0] 
	if row[0] in str(msgBody["passWord"]).strip():
		return (0, msgBody)
	else:
		print row[0] 
		print 'mismatched passwd'
		print type(str(msgBody["passWord"]).strip())
		print str(msgBody["passWord"]).strip()	
		return (0x0004, msgBody) # unmatched passwd 

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

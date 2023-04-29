
from flask import Flask,request,jsonify
from orderController import OrderConrollerClass
import json
import os

app = Flask(__name__)

@app.after_request
def add_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] =  "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"
    response.headers['Access-Control-Allow-Methods']=  "POST, GET, PUT, DELETE, OPTIONS"
    return response

@app.route("/orders/test")
def orderTest():
    return "Order service is working!!!"

@app.route("/orders/cart/add", methods=["POST"])
def addCart():
    result = OrderConrollerClass().addCart((request.get_json()))
    return jsonify(message=result)

@app.route("/orders/cart/list", methods=["POST"])
def listCart():
    return jsonify(OrderConrollerClass().listCart((request.get_json())))

@app.route("/orders/cart/delete", methods=["POST"])
def deleteCart():
    result = OrderConrollerClass().deleteCart((request.get_json()))
    print(result)
    return jsonify(message=result)

@app.route("/orders/cart/pay", methods=["POST"])
def payCart():
    result = OrderConrollerClass().payCart((request.get_json()))
    return jsonify(message=result)

@app.route("/orders/myorders", methods=["POST"])
def listMyOrders():
    return jsonify(OrderConrollerClass().listMyOrders((request.get_json())))

@app.route("/orders/all", methods=["POST"])
def listAllOrders():
    return jsonify(OrderConrollerClass().listAllOrders((request.get_json())))

@app.route("/orders/update", methods=["POST"])
def updateDeliveryStatus():
    result = OrderConrollerClass().updateDeliveryStatus((request.get_json()))
    return jsonify(message=result)

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8082)


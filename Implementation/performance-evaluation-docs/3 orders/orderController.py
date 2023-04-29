from orderModel import OrderModelClass

class OrderConrollerClass:
    def __init__(self):
        self.model = OrderModelClass()

    def addCart(self,params):
        return self.model.addCart(params)

    def listCart(self,params):
        return self.model.listCart(params)

    def deleteCart(self,params):
        return self.model.deleteCart(params)

    def payCart(self,params):
        return self.model.payCart(params)

    def listMyOrders(self,params):
        return self.model.listMyOrders(params)

    def listAllOrders(self,params):
        return self.model.listAllOrders(params)

    def updateDeliveryStatus(self,params):
        return self.model.updateDeliveryStatus(params)


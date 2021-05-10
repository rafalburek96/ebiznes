// eslint-disable-next-line
import React from "react";
 
export class Order {
    id;
    customerID;
    basketDateAdded;
    isBasketCompleted;
}

const url = "http://localhost:9000/order";

export class OrderService {

    async getAllOrders(): Promise<Order> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get basket, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createOrder(customerID, basketID, deliveryDate, purchaseDate, orderStatusCode): Promise<Order> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "customerID": customerID, "basketID": basketID,
                    "deliveryDate": deliveryDate, "purchaseDate": purchaseDate, "orderStatusCode": orderStatusCode})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create an order, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteOrder(id, customerID, basketID, deliveryDate, purchaseDate, orderStatusCode): Promise<Order> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": id, "customerID": customerID, "basketID": basketID,
                    "deliveryDate": deliveryDate, "purchaseDate": purchaseDate, "orderStatusCode": orderStatusCode})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete order, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getOrderByID(id): Promise<Order> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get order with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

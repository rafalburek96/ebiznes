// eslint-disable-next-line
import React from "react";
 
export class Basket {
    id;
    customerID;
    basketDateAdded;
    isBasketCompleted;
}

const url = "http://localhost:9000/basket";

export class BasketService {

    async getAllBaskets(): Promise<Array<Basket>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get basket, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createBasket(customerID, basketDateAdded, isBasketCompleted): Promise<Basket> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "customerID": customerID, "basketDateAdded": basketDateAdded,
                    "isBasketCompleted": isBasketCompleted})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create a basket, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteAddress(id, customerID, basketDateAdded, isBasketCompleted): Promise<Basket> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": 0, "customerID": customerID, "basketDateAdded": basketDateAdded,
                    "isBasketCompleted": isBasketCompleted})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete basket, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getBasketByID(id): Promise<Basket> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get basket with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

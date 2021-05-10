// eslint-disable-next-line
import React from "react";
 
export class Payment {
    id;
    isPaymentFinished;
    orderID;
}

const url = "http://localhost:9000/payment";

export class PaymentService {

    async getAllPayments(): Promise<Array<Payment>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get payment, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createPayment(isPaymentFinished, orderID): Promise<Payment> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "isPaymentFinished": isPaymentFinished, "orderID": orderID})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create an payment, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deletePayment(id, isPaymentFinished, orderID): Promise<Payment> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": 0, "isPaymentFinished": isPaymentFinished, "orderID": orderID})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete payment, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getPaymentByID(id): Promise<Payment> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get payment with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

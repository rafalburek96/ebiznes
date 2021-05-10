// eslint-disable-next-line
import React from "react";
 
export class Customer {
    id;
    customerID;
    basketDateAdded;
    isBasketCompleted;
}

const url = "http://localhost:9000/basket";

export class CustomerService {

    async createCustomer(name, surname, email, phoneNumber, password, address): Promise<Customer> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "name": name, "surname": surname,
                    "email": email, "phoneNumber": phoneNumber, "password": password,
                    "address": address})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create a customer, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteCustomer(id, name, surname, email, phoneNumber, password, address): Promise<Customer> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": id, "name": name, "surname": surname,
                    "email": email, "phoneNumber": phoneNumber, "password": password,
                    "address": address})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete customer, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getCustomerByID(id): Promise<Customer> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get customer with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

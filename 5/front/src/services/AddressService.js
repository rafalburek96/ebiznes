// eslint-disable-next-line
import React from "react";
 
export class Address {
    id;
    street;
    flatNumber;
    zipCode;
}

const url = "http://localhost:9000/address";

export class AddressService {

    async getAllAddresses(): Promise<Array<Address>> {
        let res;
        try {
            res = await fetch(url);
            // console.log('SERVICE: ' + JSON.parse(res.text()) );
        } catch(error) {
            console.log('Cannnot get an address, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createAddress(street, flatNumber, zipCode): Promise<Address> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "street": street, "flatNumber": flatNumber, "zipCode": zipCode})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create an address, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async updateAddress(id, street, flatNumber, zipCode): Promise<Address> {
        let res;
        try {
            var opt = {
                'method': 'PUT',
                body: JSON.stringify({"id": id, "street": street, "flatNumber": flatNumber, "zipCode": zipCode})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot update an address, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteAddress(id, street, flatNumber, zipCode): Promise<Address> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": id, "street": street, "flatNumber": flatNumber, "zipCode": zipCode})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete an address, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getAddressByID(id): Promise<Address> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get an address with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

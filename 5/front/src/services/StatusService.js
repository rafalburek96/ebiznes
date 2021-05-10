// eslint-disable-next-line
import React from "react";
 
export class Status {
    id;
    orderStatusCode;
    orderStatusDescription;
}

const url = "http://localhost:9000/order-status";

export class StatusService {

    async getAllStatuses(): Promise<Array<Status>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get Status, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createStatus(orderStatusCode, orderStatusDescription): Promise<Status> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "orderStatusCode": orderStatusCode, "orderStatusDescription": orderStatusDescription})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create an status, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteStatus(id, orderStatusCode, orderStatusDescription): Promise<Status> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": id, "orderStatusCode": orderStatusCode, "orderStatusDescription": orderStatusDescription})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete status, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getStatusByID(id): Promise<Status> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get status with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

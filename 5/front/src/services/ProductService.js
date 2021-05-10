// eslint-disable-next-line
import React from "react";
 
export class Product {
    id;
    productName;
    productDescription;
    productPrice;
    category;
}

const url = "http://localhost:9000/product";

export class ProductService {

    async getAllProducts(): Promise<Array<Product>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get product, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createProduct(productName, productDescription, productPrice, category): Promise<Product> {
        let res;
        try {
            let opt = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({"id": 0,
                                            "productName": productName,
                                            "productDescription": productDescription,
                                            "productPrice": productPrice,
                                            "category": category})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create the product, error: ' + error);
        }
        return await res.text();
    }

    async deleteProduct(id): Promise<Product> {
        let res;
        try {
            var opt = {
                method: 'DELETE'
            }
            res = await fetch(url + '/' + id, opt);
            console.log("RES IN DEL: ", res);
        } catch(error) {
            console.log('Cannnot delete product, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getProductByID(id): Promise<Product> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get product with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

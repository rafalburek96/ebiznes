// eslint-disable-next-line
import React from "react";
 
export class Review {
    id;
    reviewRating;
    productID;
    customerID;
}

const url = "http://localhost:9000/review";

export class ReviewService {

    async getAllReviews(): Promise<Array<Review>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannnot get review, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async createReview(reviewRating, productID, customerID): Promise<Review> {
        let res;
        try {
            var opt = {
                method: 'POST',
                body: JSON.stringify({"id": 0, "reviewRating": reviewRating, "productID": productID,
                    "customerID": customerID})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot create an review, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async deleteReview(id, reviewRating, productID, customerID): Promise<Review> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify({"id": id, "reviewRating": reviewRating, "productID": productID,
                    "customerID": customerID})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete review, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getReviewByID(id): Promise<Review> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get review with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

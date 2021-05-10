// eslint-disable-next-line
import React from "react";
 
export class Category {
    id;
    categoryName;
    categoryDescription;
}

const url = "http://localhost:9000/category";

export class CategoryService {

    async getAllCategories(): Promise<Array<Category>> {
        let res;
        try {
            res = await fetch(url);
        } catch(error) {
            console.log('Cannot get category, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    // async createCategory(categoryName, categoryDescription): Promise<Category> {
    //     let res;
    //     try {
    //         var opt = {
    //             method: 'POST',
    //             body: JSON.stringify(
    //                 {"id": 0, "categoryName": categoryName, "categoryDescription": categoryDescription})
    //         }
    //         res = await fetch(url, opt);
    //     } catch(error) {
    //         console.log('Cannnot create category, error: ' + error);
    //     }
    //     return this.getAllCategories();
    // }

    async deleteCategory(id, categoryName, categoryDescription): Promise<Category> {
        let res;
        try {
            var opt = {
                'method': 'DELETE',
                body: JSON.stringify(
                    {"id": 0, "categoryName": categoryName, "categoryDescription": categoryDescription})
            }
            res = await fetch(url, opt);
        } catch(error) {
            console.log('Cannnot delete category, error: ' + error);
        }
        return JSON.parse(await res.text());
    }

    async getCategoryByID(id): Promise<Category> {
        let res;
        try {
            res = await fetch(url + '/' + id);
        } catch(error) {
            console.log('Cannnot get categpry with given ID, error: ' + error);
        }
        return JSON.parse(await res.text());
    }
}

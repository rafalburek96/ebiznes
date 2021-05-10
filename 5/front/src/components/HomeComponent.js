// eslint-disable-next-line
import React, { Component } from "react";
import {ProductComponent} from "./ProductComponent";
import {CategoryComponent} from "./CategoryComponent";
import {AddNewProductComponent} from "./AddNewProductComponent";
  
export class HomeComponent extends Component {

    render() {

        return (
            <div>
                <CategoryComponent></CategoryComponent>
                <ProductComponent></ProductComponent>
                <AddNewProductComponent></AddNewProductComponent>
            </div>
        );
    }
}

import React, { Component } from "react";
import { CustomerService } from "../services/CustomerService";
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import {CategoryComponent} from "./CategoryComponent";
 
export class CustomerComponent extends Component {

    customerService: CustomerService;

    constructor() {
        super();
        this.customerService = new CustomerService();
        this.state = {customer: []};
    }

    async componentDidMount() {
        let res = await this.customerService.getCustomerByID(2)
        this.setState({customer: res});
    }

    render() {
        let styling = {
            margin: '0 auto',
            width: '18rem'
        }

        return (
            <div>
                <CategoryComponent></CategoryComponent>
                <Card style={styling}>
                    <Card.Img variant="top" src={require('../img/user.png')}/>
                    <Card.Body>
                        <Card.Title>Anna Kowalska</Card.Title>
                        <Card.Text>
                          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                        </Card.Text>
                        <Button variant="secondary"  href="/logIn">Log out ></Button>{'  '}
                        <Button variant="danger">Delete Account</Button>
                    </Card.Body>
                </Card>
            </div>

        );
    }
}

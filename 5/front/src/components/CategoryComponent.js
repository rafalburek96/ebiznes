import React, { Component } from "react";
import { CategoryService } from "../services/CategoryService";
import NavDropdown from 'react-bootstrap/NavDropdown'
import Nav from "react-bootstrap/Nav";

 
export class CategoryComponent extends Component {

    categoryService: CategoryService;

    constructor() {
        super();
        this.categoryService = new CategoryService();
        this.state = {category: []};
    }

    async componentDidMount() {
        let res = await this.categoryService.getAllCategories();
        this.setState({category: res});
    }

    render() {

        let dropDown = this.state.category.map( c => (
            <NavDropdown.Item eventKey="4.1">{ c.categoryName }</NavDropdown.Item>
        ));

        return (
            <Nav defaultActiveKey="/home" as="ul" variant="pills">
                <Nav.Item as="li">
                    <Nav.Link href="/">Home</Nav.Link>
                </Nav.Item>
                <NavDropdown title="Category" id="nav-dropdown">
                { dropDown }
                </NavDropdown>
                <Nav.Item as="li">
                    <Nav.Link href="/customer">Customer</Nav.Link>
                </Nav.Item>
                <Nav.Item as="li">
                    <Nav.Link href="/ordered-products">My basket</Nav.Link>
                </Nav.Item>
                <Nav.Item as="li">
                    <Nav.Link href="http://localhost:9000/signIn">Log Out</Nav.Link>
                </Nav.Item>
            </Nav>
        );
    }
}

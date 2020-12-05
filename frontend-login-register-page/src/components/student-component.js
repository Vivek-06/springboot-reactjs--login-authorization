import React, {Component} from "react";
import UserService from "../service/user-service"
import {Button} from "react-bootstrap";

export default class BoardStudent extends Component {
    constructor(props){
        super(props);
        this.state= {
            content: ""
        }
    }

    componentDidMount() {
        UserService.getStudentBoard().then(
            response => {
                this.setState({
                    content: response.data
                });
            },
            error => {
                this.setState({
                    content: (
                        error.response && error.response.data &&
                        error.response.data.message
                    ) || error.message || error.toString()
                });
            }
        );
    }

    render() {
        return (
            <div className="container">
                <header className="jumbotron">
                    <h3>{this.state.content}</h3>

                </header>
                <h1>Student Component</h1>
                <Button>SUbmit</Button>
            </div>
        );
    }
}
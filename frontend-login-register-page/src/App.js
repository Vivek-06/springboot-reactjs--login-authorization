import './App.css';
import React, {Component} from "react";
import AuthService from "./service/auth-service"
import {Link, Route, Switch} from "react-router-dom";
import Login from "./components/login-component";
import Home from "./components/home";
import Register from "./components/register";
import Profile from "./components/profile";
import BoardStudent from "./components/student-component";
import BoardFaculty from "./components/faculty-component";
import BoardAdmin from "./components/admin-component";

class App extends Component{
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);

        this.state = {
            showStudentBoard: false,
            showAdminBoard: false,
            showFacultyBoard: false,
        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();
        if (user) {
            this.setState({
                showStudentBoard: user.roles.includes("ROLE_STUDENT"),
                showAdminBoard: user.roles.includes("ROLE_ADMIN"),
                showFacultyBoard: user.roles.includes("ROLE_FACULTY"),
            });
        }
    }

    logOut(){
        AuthService.logout();
    }

    render() {
        const {showStudentBoard, showAdminBoard, showFacultyBoard} = this.state;

        return (
            <div>
                <nav className="navbar navbar-expand navbar-dark bg-dark">
                    <Link to={"/"} className="navbar-brand">Vivek</Link>
                    <div className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <Link to={"/home"} className="nav-link">
                                Home
                            </Link>
                        </li>

                        {showFacultyBoard && (
                            <li className="nav-item">
                                <Link to={"/faculty"} className="nav-link">
                                    Faculty Board
                                </Link>
                            </li>
                        )}

                        {showAdminBoard && (
                            <li className="nav-item">
                                <Link to={"/admin"} className="nav-link">
                                    Admin Board
                                </Link>
                            </li>
                        )}

                        {showStudentBoard && (
                            <li className="nav-item">
                                <Link to={"/student"} className="nav-link">
                                    Student
                                </Link>
                            </li>
                        )}
                    </div>

                        {showStudentBoard || showFacultyBoard ||showAdminBoard? (
                            <div className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <Link to={"/profile"} className="nav-link">
                                        Profile
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <a href="/login" className="nav-link" onClick={this.logOut}>
                                        LogOut
                                    </a>
                                </li>
                            </div>
                        ) : (
                            <div className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <Link to={"/login"} className="nav-link">
                                        Login
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link to={"/signup"} className="nav-link">
                                        Register
                                    </Link>
                                </li>
                            </div>
                        )}
                </nav>
                <div className="container m-3">
                    <Switch>
                        <Route exact path={["/", "/home"]} component={Home} />
                        <Route exact path="/login" component={Login} />
                        <Route exact path="/signup" component={Register} />
                        <Route exact path="/profile" component={Profile} />
                        <Route path="/student" component={BoardStudent} />
                        <Route path="/faculty" component={BoardFaculty} />
                        <Route path="/admin" component={BoardAdmin} />
                    </Switch>
                </div>
            </div>
    )
    }
    }

    export default App;

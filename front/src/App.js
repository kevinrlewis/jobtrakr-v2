import React from 'react';
import logo from './logo.svg';
import './App.css';
import {
    AppBar, Button, Toolbar,
    IconButton, Typography, Container,
    Grid, CircularProgress
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { Auth0Provider } from "@auth0/auth0-react";

import MenuIcon from '@material-ui/icons/Menu';

import { useAuth0 } from "@auth0/auth0-react";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
}));

function App() {
    const { user, isAuthenticated, isLoading, logout, loginWithRedirect } = useAuth0();
    const classes = useStyles();
    console.log(user);

    let button;
    if(isAuthenticated) {
        button = <Button variant='outlined' color='inherit' onClick={() => logout({ returnTo: window.location.origin })}>Logout</Button>
    } else {
        button = <Button variant='outlined' color='inherit' onClick={() => loginWithRedirect()}>Login</Button>
    }

    let app;
    if(!isLoading) {
        app = (
            <AppBar position="static">
                <Toolbar>
                    <IconButton className={classes.menuButton} edge="start" color="inherit" aria-label="menu">
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" className={classes.title}>
                        Jobtrakr
                    </Typography>
                    {button}
                </Toolbar>
            </AppBar>
        );
    } else {
        app = (
            <Container>
                <Grid container
                    direction="row"
                    justify="center"
                    alignItems="center"
                    style={{
                        position: 'absolute', left: '50%', top: '50%',
                        transform: 'translate(-50%, -50%)'
                    }}
                >
                    <Grid item>
                        <CircularProgress />
                    </Grid>
                </Grid>
            </Container>
        );
    }

    return (
        <div className={classes.root}>
            {app}
        </div>
    );
}

export default App;

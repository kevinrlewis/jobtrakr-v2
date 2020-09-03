import Head from 'next/head';
import React from 'react';
import Link from 'next/link';
import { useRouter } from 'next/router'
import { Container, Grid, Button } from '@material-ui/core';

class Home extends React.Component {
    constructor(props) {
        super(props);
        console.debug('home props:', props);
        this.state = {
        }
    }

    render() {
        return (
            <div>
                <Head>
                    <title>Jobtrakr</title>
                    <link rel="icon" href="/favicon.ico" />
                </Head>

                <main>
                    <Container>
                        <Grid container>
                            <Grid item xs={6}>
                                <a href="/api/login">Login</a>
                            </Grid>
                        </Grid>
                    </Container>
                </main>
            </div>
        );
    }
}


export default Home;

import Head from 'next/head';
import React from 'react';
import auth0 from './api/auth0';
import { Auth0Provider } from 'use-auth0-hooks';

class Jobs extends React.Component {
    constructor(props) {
        super(props);
        console.debug('jobs props:', props);

        this.state = {}

        console.debug(this.state);
    }

    async componentDidMount() {
        const res = await fetch('/api/me');
        if (res.ok) {
            this.setState({
                session: await res.json()
            });
            console.debug(this.state.session);
        } else {
            console.debug(res);
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
                </main>

                <footer>
                    <a
                    href="https://vercel.com?utm_source=create-next-app&utm_medium=default-template&utm_campaign=create-next-app"
                    target="_blank"
                    rel="noopener noreferrer"
                    >
                        <img src="/vercel.svg" alt="Vercel Logo" />
                    </a>
                </footer>
            </div>
        );
    }
}


export default Jobs;

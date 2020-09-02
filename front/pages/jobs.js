import Head from 'next/head';
import React from 'react';
import auth0 from './api/security/auth0';
import { Auth0Provider } from 'use-auth0-hooks';

class Jobs extends React.Component {
    constructor(props) {
        super(props);
        console.debug('jobs props:', props);

        this.state = {}

        console.debug(this.state);
        console.log(this.state);
    }

    render() {
        return (
            <Auth0Provider
                domain='dev-6w69949q.us.auth0.com'
                clientId='RHuQaYpKxdRVOWztAzz1DWIOD6DMfAj5'
                redirectUri='http://localhost:3000/jobs'
            >
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
            </Auth0Provider>
        );
    }
}


export default Jobs;

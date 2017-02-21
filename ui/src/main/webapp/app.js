import React from 'react'
import { Provider } from 'react-redux'
import store from './store'

import Exception from './containers/exceptions'
import Ldap from './wizards/ldap'
import Sources from './wizards/sources'
import { Home } from './home'
import Wcpm from './adminTools/webContextPolicyManager'

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider'
import HomeIcon from 'material-ui/svg-icons/action/home'
import IconButton from 'material-ui/IconButton'
import { Link } from 'react-router'
import AppBar from 'material-ui/AppBar'

import Banners from 'system-usage/Banners'
import Modal from 'system-usage/Modal'

import Relay from 'react-relay'

function graphQLFetcher (graphQLParams) {
  return window.fetch(window.location.origin + '/graphql', {
    method: 'post',
    credentials: 'same-origin',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(graphQLParams)
  }).then(response => response.json())
}

const GraphiQL = () => {
  require('graphiql/graphiql.css')
  const GraphiQL = require('graphiql')
  return <GraphiQL fetcher={graphQLFetcher} />
}
var DevTools

if (process.env.NODE_ENV === 'production') {
  DevTools = () => null
}

if (process.env.NODE_ENV !== 'production') {
  DevTools = require('./containers/dev-tools').default
}

const LinkHomeIcon = (props) => (
  <Link to='/'>
    <HomeIcon {...props} />
  </Link>
)

const relayRoute = {
  name: 'relayRoute',
  queries: {
    ldap: () => Relay.QL`
      query  {
        ldap
      }
    `,
    sts: () => Relay.QL`
      query {
        sts
      }
    `
  },
  params: {}
}

const Sts = Relay.createContainer(({ sts = 'Not found' }) => {
  return (
    <pre>
      {JSON.stringify(sts.claims, null, 2)}
    </pre>
  )
}, {
  fragments: {
    sts: () => Relay.QL`
      fragment on SecurityTokenService {
        claims
      }
    `
  }
})

const Random = Relay.createContainer(({ ldap, children }) => {
  return (
    <div>
      <pre>
        {JSON.stringify(ldap.testConnect.successes[0].code, null, 2)}
      </pre>
    </div>
  )
}, {
  initialVariables: {
    conn: null
  },
  fragments: {
    ldap: () => Relay.QL`
      fragment on Ldap {
        testConnect {
          successes {
            code
            content
          }
        }
      }
    `
  }
})

const App = ({ children }) => (
  <MuiThemeProvider>
    <Provider store={store}>
      <Banners>
        <Modal />
        <AppBar
          iconElementLeft={
            <IconButton>
              <LinkHomeIcon />
            </IconButton>
          } />
        <div style={{ maxWidth: 960, padding: 20, margin: '0 auto' }}>{children}</div>
        <Exception />
        <DevTools />
      </Banners>
    </Provider>
  </MuiThemeProvider>
)

export const routes = {
  path: '/',
  component: App,
  indexRoute: { component: Home },
  childRoutes: [
    { path: 'ldap', component: Ldap },
    { path: 'sources', component: Sources },
    { path: 'web-context-policy-manager', component: Wcpm },
    { path: 'graphiql', component: GraphiQL }
  ]
}

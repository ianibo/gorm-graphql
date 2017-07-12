package gorm.graphql

import grails.plugins.*
import graphql.GraphQL
import org.grails.gorm.graphql.Schema
import org.grails.gorm.graphql.binding.GraphQLDataBinder
import org.grails.gorm.graphql.binding.manager.GraphQLDataBinderManager
import org.grails.gorm.graphql.entity.GraphQLEntityNamingConvention
import org.grails.gorm.graphql.fetcher.manager.DefaultGraphQLDataFetcherManager
import org.grails.gorm.graphql.response.delete.DefaultGraphQLDeleteResponseHandler
import org.grails.gorm.graphql.response.errors.DefaultGraphQLErrorsResponseHandler
import org.grails.gorm.graphql.types.DefaultGraphQLTypeManager
import org.springframework.beans.MutablePropertyValues
import org.springframework.validation.DataBinder

class GormGraphqlGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.2.9 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Gorm Graphql" // Headline display name of the plugin
    def author = "James Kleeh"
    def authorEmail = "james.kleeh@gmail.com"
    def description = '''\
Brief summary/description of the plugin.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/gorm-graphql"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() {{ ->
        errorsResponseHandler(DefaultGraphQLErrorsResponseHandler, ref("messageSource"))
        deleteResponseHandler(DefaultGraphQLDeleteResponseHandler)
        namingConvention(GraphQLEntityNamingConvention)
        typeManager(DefaultGraphQLTypeManager, ref("namingConvention"))
        dataBinderManager(GraphQLDataBinderManager, new DefaultGraphQLDataBinder())
        dataFetcherManager(DefaultGraphQLDataFetcherManager)

        customSchema(Schema, ref("grailsDomainClassMappingContext")) {
            errorsResponseHandler = ref("errorsResponseHandler")
            deleteResponseHandler = ref("deleteResponseHandler")
            namingConvention = ref("namingConvention")
            typeManager = ref("typeManager")
            dataBinderManager = ref("dataBinderManager")
            dataFetcherManager = ref("dataFetcherManager")
        }
        graphQLSchema(customSchema: "generate")
        graphQL(GraphQL, ref("graphQLSchema"))


    }}
}

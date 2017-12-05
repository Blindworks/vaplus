/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.vaplus.client.picketlink;

import javax.enterprise.event.Observes;

import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

/**
 * <p>A simple CDI observer for the {@link org.picketlink.event.SecurityConfigurationEvent}.</p>
 *
 * <p>The event is fired during application startup and allows you to provide any configuration to PicketLink
 * before it is initialized.</p>
 *
 * <p>All the configuration related with Http Security is provided from this bean.</p>
 *
 * @author Pedro Igor
 */
public class HttpSecurityConfiguration {

	public void onInit(@Observes SecurityConfigurationEvent event) {
        SecurityConfigurationBuilder builder = event.getBuilder();

        builder
        	.http()
                .allPaths()
                    .authenticateWith()
                        .form()
                        .authenticationUri("/login.xhtml")
                        .loginPage("/login.xhtml")
//                        .errorPage("/login.xhtml")
//                        .restoreOriginalRequest()
//                        .loginPage("/login.xhtml")


//                            .authenticationUri("/login.xhtml")
//                            .loginPage("/login.xhtml")
//                            .errorPage("/login.xhtml")
                .forPath("/javax.faces.resource/*")
                    	.unprotected()

                .forPath("/resources/icon/*")
                    	.unprotected()

                .forPath("/browserconfig.xml")
                    	.unprotected()

                .forPath("/manifest.json")
                    	.unprotected()

                .forPath("/favicon.ico")
                    	.unprotected()

                .forPath("/robots.txt")
                    	.unprotected()

                .forPath("/lostPassword.xhtml")
                    	.unprotected()

                .forPath("/lostUsername.xhtml")
                    	.unprotected()

                .forPath("/l.xhtml")
                    	.unprotected()

                .forPath("/test.xhtml")
                    	.unprotected()

                .forPath("/test/*")
                    	.unprotected()

                .forPath("/sl/*")
                    	.unprotected()

                .forPath("/sockets/ical/*")
                        .unprotected()

                .forPath("/sockets/calendar/*")
                        .unprotected()

                .forPath("/error/*")
                    	.unprotected()

                .forPath("/logout")
                    .logout()
                    .redirectTo("/login.xhtml");
//                .forPath("/settings/shopList.xhtml")
//                	.authorizeWith()
//                		.role("Administrator");




        // Autorisation

        builder.http()

        .forPath("/customer/*")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('customer','read')}")

        .forPath("/customer/contract.xhtml")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('contract','create')}")

        .forPath("/customer/order.xhtml")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('order','create')}")

        .forPath("/customer/customer.xhtml")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('customer','edit')}")

        .forPath("/newCustomer.xhtml")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('customer','create')}")

        .forPath("/stats/*")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('controlling','read')}")

        .forPath("/stats/contractList.xhtml")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('contract_list','read') or permissionBeanWrapper.hasPermission('contract_list','read_own')}")

        .forPath("/calendar/*")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('calendar','read')}")

        .forPath("/management/*")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('management','read')}")

        .forPath("/settings/*")
        	.authorizeWith()
        		.expression("#{permissionBeanWrapper.hasPermission('settings','read')}")

        .forPath("/support/*")
    		.authorizeWith()
    			.expression("#{permissionBeanWrapper.hasPermission('support','read')}");

//        builder.http()
//        .forPath("/test.xhtml")
//        	.authorizeWith()
//        		.expression("#{permissionBeanWrapper.hasPermission('test','read')}");

//        builder.http()
//			.forPath("/calendar/*")
//	    	.authorizeWith()
//	        	.authorizer(PickeltLinkPathAuthorizer.class)
//    		.forPath("/chat/*")
//        	.authorizeWith()
//            	.authorizer(PickeltLinkPathAuthorizer.class)
//    		.forPath("/customer/*")
//            	.authorizeWith()
//                	.authorizer(PickeltLinkPathAuthorizer.class)
//            		.forPath("/followUp/*")
//            		.authorizeWith()
//            			.authorizer(PickeltLinkPathAuthorizer.class)
//                		.forPath("/management/*")
//                		.authorizeWith()
//                			.authorizer(PickeltLinkPathAuthorizer.class)
//                    		.forPath("/setting/*")
//                    		.authorizeWith()
//                    			.authorizer(PickeltLinkPathAuthorizer.class)
//                        		.forPath("/stats/*")
//                        		.authorizeWith()
//                        			.authorizer(PickeltLinkPathAuthorizer.class)
//                            		.forPath("/support/*")
//                            		.authorizeWith()
//                            			.authorizer(PickeltLinkPathAuthorizer.class)
//                                		.forPath("/user/*")
//                                		.authorizeWith()
//                                			.authorizer(PickeltLinkPathAuthorizer.class)
//
//                	;

//        builder.http()
//        	.forPath("/settings/*")
//        		.authorizeWith()
//        			.expression("#{hasPermission(String.class,'user_profile','read')}");
//        			.role("Supervisor")
//        		.forPath("/*")
//​        			.authorizeWith()
//​            			.authorizer(RealmPathAuthorizer.class);
//        	.forPath("/management/*")
//        		.authorizeWith()
//        			.role("Supervisor");


//    	.identity()
//    		.scope(WindowScoped.class)

    }

}

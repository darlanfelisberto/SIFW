//package br.edu.iffar.fw.classBag.sec;
//
//
//import org.keycloak.KeycloakPrincipal;
//
///**
// * Copyright 2015-2017 Red Hat, Inc, and individual contributors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//
///**
// * @author Bob McWhirter
// */
//@SuppressWarnings("rawtypes")
//public class KeycloakPrincipalContext__ {
//
//    protected KeycloakPrincipalContext__() {
//    }
//
//    public static KeycloakPrincipal get() {
//        return SECURITY_CONTEXT.get();
//    }
//
//
//	public static void associate(KeycloakPrincipal context) {
//        SECURITY_CONTEXT.set(context);
//    }
//
//    public static void disassociate() {
//        SECURITY_CONTEXT.remove();
//    }
//
//    private static ThreadLocal<KeycloakPrincipal> SECURITY_CONTEXT = new ThreadLocal<>();
//}
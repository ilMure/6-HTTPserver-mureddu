package it.fi.meucci;

import it.fi.meucci.HTTPserver.MultiServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MultiServer HTTPserver = new HTTPserver.MultiServer();
        HTTPserver.serverStart();
    }
}

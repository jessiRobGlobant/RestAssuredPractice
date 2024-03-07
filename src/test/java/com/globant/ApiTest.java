package com.globant;

import io.restassured.RestAssured;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ApiTest
    extends TestCase
{
    private static final String CONTENT_TYPE_KEY = "Content Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ApiTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ApiTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public static void TestConfig(){
        RestAssured.baseURI = "https://63b6dfe11907f863aa04ff81.mockapi.io";
    }
}

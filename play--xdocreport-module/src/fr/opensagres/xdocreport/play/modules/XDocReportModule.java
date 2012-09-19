package fr.opensagres.xdocreport.play.modules;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Ok;
import play.mvc.results.Result;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.XDocReport;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportController;
import fr.opensagres.xdocreport.document.dispatcher.IXDocReportLoader;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReportModule
{

    public static Result generateReport( IXDocReport report, IContext context )

    {
        MimeMapping mimeMapping = report.getMimeMapping();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			report.process( context, out );
			return ok( out.toByteArray(), mimeMapping );
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return null;
    }

    public static Result convert( IXDocReport report, IContext context, Options options )
        throws XDocReportException, IOException
    {
        IConverter converter = report.getConverter( options );
        MimeMapping mimeMapping = converter.getMimeMapping();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        report.convert( context, options, out );
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result ok( final byte[] content, final MimeMapping mimeMapping )
        throws IOException
    {

    	return new Result() {

			@Override
			public void apply(Request request, Response response) {
				response.setContentTypeIfNotSet(mimeMapping.getMimeType());
				try {
					response.out.write(content);
				} catch (IOException e) {
					response.setContentTypeIfNotSet("text/plain");
					response.print(e.getMessage());
				}

			}
		};
        //return Results.ok( content ).as( mimeMapping.getMimeType() );
    }

    public static Result generateReport( String reportId, IXDocReportController controller,
                                         Map<String, Object> contextMap )
        throws IOException, XDocReportException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IXDocReport report = XDocReport.generateReport( reportId, controller, contextMap, out );
        MimeMapping mimeMapping = report.getMimeMapping();
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result generateReportAndConvert( String reportId, IXDocReportController reportController,
                                                   Map<String, Object> contextMap, Options options )
        throws IOException, XDocReportException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IXDocReport report = XDocReport.generateReportAndConvert( reportId, reportController, contextMap, options, out );
        IConverter converter = report.getConverter( options );
        MimeMapping mimeMapping = converter.getMimeMapping();
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result generateReport( String reportId, IXDocReportLoader reportLoader, Map<String, Object> contextMap )
        throws IOException, XDocReportException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IXDocReport report = XDocReport.generateReport( reportId, reportLoader, contextMap, out );
        MimeMapping mimeMapping = report.getMimeMapping();
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result generateReportAndConvert( String reportId, IXDocReportLoader reportLoader,
                                                   Map<String, Object> contextMap, Options options )
        throws IOException, XDocReportException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IXDocReport report = XDocReport.generateReportAndConvert( reportId, reportLoader, contextMap, options, out );
        IConverter converter = report.getConverter( options );
        MimeMapping mimeMapping = converter.getMimeMapping();
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result generateReport( InputStream resourceAsStream, String templateEngineKind,
                                         FieldsMetadata metadata, Map<String, Object> contextMap )
        throws XDocReportException, IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IXDocReport report =
            XDocReport.generateReport( resourceAsStream, templateEngineKind, metadata, contextMap, out );
        MimeMapping mimeMapping = report.getMimeMapping();
        return ok( out.toByteArray(), mimeMapping );
    }

    public static Result generateReport( InputStream resourceAsStream, TemplateEngineKind templateEngineKind,
                                         FieldsMetadata metadata, Map<String, Object> contextMap )
        throws XDocReportException, IOException
    {
        return generateReport( resourceAsStream, templateEngineKind.name(), metadata, contextMap );
    }

}

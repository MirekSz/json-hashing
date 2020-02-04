import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.SAXException;

public class BarCode {

	public static void main(final String[] args) throws Exception, BarcodeException, SAXException, IOException {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();

		Configuration cfg =
				builder.buildFromFile(new File("C:\\Users\\MirekSz\\git\\json-hashing\\json-hashing\\src\\main\\resources\\Code128B.xml"));

		BarcodeGenerator gen = BarcodeUtil.getInstance().createBarcodeGenerator(cfg);

		OutputStream out = new java.io.FileOutputStream(new File("output.png"));
		BitmapCanvasProvider provider = new BitmapCanvasProvider(out, "image/x-png", 300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
		gen.generateBarcode(provider, "40050444039-19012");
		provider.finish();

		DocumentFragment frag = BarcodeUtil.getInstance().generateSVGBarcode(cfg, "40050444039-19012");

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer trans = factory.newTransformer();
		Source src = new DOMSource(frag);
		Result res = new StreamResult(new File("output.svg"));
		trans.transform(src, res);
	}

}

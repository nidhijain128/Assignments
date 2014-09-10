import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ImageData {
	// Actual pixel values
	public int[][] data;
	// Number rows, will always be 28
	public int rows;
	// Number of cols, will always be 28
	public int cols;
	// Digit (0-9) that is being shown in this image
	public int label;

	// declaring static variables
	static int noOfInputNodes = 784;
	static int noOfHiddenLayer1Nodes = 252;
	static int noOfHiddenLayer2Nodes = 83;
	static int noOfOutputNodes = 10;
	static double weightsLevel1[][] = new double[noOfInputNodes][noOfHiddenLayer1Nodes];
	static double weightsLevel2[][] = new double[noOfHiddenLayer1Nodes][noOfHiddenLayer2Nodes];
	static double weightsLevel3[][] = new double[noOfHiddenLayer2Nodes][noOfOutputNodes];
	static double speed = 0.08;

	// declaring arrays to store input and output values at all layers
	double inputNodes[];
	double inputHiddenLayer1[];
	double outputHiddenLayer1[];
	double inputHiddenLayer2[];
	double outputHiddenLayer2[];
	double outputNodesInput[];
	double outputNodesOutput[];
	double errorOutput[];
	double errorHidden1[];
	double errorHidden2[];

	// Variable for storing the prediction of your network (only used for test
	// images).
	// You do not need to store your predictions here, this is simply for
	// convenience.
	public int prediction;

	// generates random weights at a level
	public static double[][] generateRandom(double a[][], double min, double max) {
		double range = max - min;
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++) {
				double random = Math.random();
				double adjustment = range * random;
				double res = min + adjustment;
				a[i][j] = res;
			}
		return a;
	}

	// generates input values of the nodes at a particular layer
	public double[] generateInputValue(double start[], double edge[][],
			double end[]) {
		for (int i = 0; i < end.length; i++)
			for (int j = 0; j < start.length; j++) {
				end[i] += start[j] * edge[j][i];
			}
		return end;
	}

	// generates output values using the input values at a layer
	public double[] generateOutputValue(double input[]) {
		for (int i = 0; i < input.length; i++)
			input[i] = 1.0 / (1.0 + Math.exp(-1.0 * input[i]));
		return input;
	}

	// calculates the error at the output nodes
	public void calculateOutputError() {
		double target[] = new double[10];
		target[label] = 1.0;
		for (int i = 0; i < outputNodesOutput.length; i++)
			errorOutput[i] = outputNodesOutput[i] * (1 - outputNodesOutput[i])
					* (target[i] - outputNodesOutput[i]);
	}

	// changs the weight values using the error value
	public double[][] changeWeight(double error[], double weights[][],
			double output[]) {
		for (int i = 0; i < error.length; i++)
			for (int j = 0; j < output.length; j++) {
				weights[j][i] = weights[j][i] + (speed * error[i] * output[j]);
			}
		return weights;
	}

	// calculates error value at all the other layers except the output layer
	public double[] calculateHiddenError(double weights[][], double output[],
			double error[], double errorHidden[]) {
		for (int i = 0; i < output.length; i++) {
			double errorValue = 0.0;
			for (int j = 0; j < error.length; j++) {
				errorValue = errorValue + error[j] * weights[i][j];
			}
			errorHidden[i] = output[i] * (1 - output[i]) * errorValue;
		}
		return errorHidden;
	}

	public ImageData(int rows, int cols, int[][] data) {
		this.rows = rows;
		this.cols = cols;
		this.data = data;
		this.label = -1; // This will be set in readAndApplyLabels()
		this.prediction = -1;

		// initializing the arrays with appropriate sizes
		inputNodes = new double[noOfInputNodes];
		inputHiddenLayer1 = new double[noOfHiddenLayer1Nodes];
		outputHiddenLayer1 = new double[noOfHiddenLayer1Nodes];
		inputHiddenLayer2 = new double[noOfHiddenLayer2Nodes];
		outputHiddenLayer2 = new double[noOfHiddenLayer2Nodes];
		errorHidden1 = new double[noOfHiddenLayer1Nodes];
		errorHidden2 = new double[noOfHiddenLayer2Nodes];
		outputNodesInput = new double[noOfOutputNodes];
		outputNodesOutput = new double[noOfOutputNodes];
		errorOutput = new double[noOfOutputNodes];

		// storing pixel information into inputNodes array
		int k = 0;
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[0].length; j++) {
				inputNodes[k] = data[i][j] / 100.0;
				k++;
			}
	}

	// Use this method to load training data
	public static ArrayList<ImageData> readData(String imagesPath,
			String labelsPath) {
		ArrayList<ImageData> images = readImageFile(imagesPath);
		readAndApplyLabels(labelsPath, images);
		return images;
	}

	private static ArrayList<ImageData> readImageFile(String imagesPath) {
		ArrayList<ImageData> images = new ArrayList<ImageData>();
		try {
			// Open file and get bytes
			Path path = Paths.get(imagesPath);
			byte[] data = Files.readAllBytes(path);
			ByteBuffer buffer = ByteBuffer.wrap(data);

			// Ignore first 4-byte int
			buffer.getInt();

			// Read metadata values (4-byte ints)
			int numImages = buffer.getInt();
			int rows = buffer.getInt();
			int cols = buffer.getInt();

			// Read image values (1-byte ints)
			for (int imageIndex = 0; imageIndex < numImages; imageIndex++) {
				int[][] imageVals = new int[rows][cols];
				for (int c = 0; c < cols; c++) {
					for (int r = 0; r < rows; r++) {
						int val = buffer.get() & 0xFF; // Get unsigned int value
						imageVals[r][c] = val;
					}
				}
				images.add(new ImageData(rows, cols, imageVals));
			}
		} catch (IOException e) {
			System.out.println("Error reading training images!");
			return null;
		}
		return images;
	}

	private static void readAndApplyLabels(String labelsPath,
			ArrayList<ImageData> images) {
		try {
			// Open file and get bytes
			Path path = Paths.get(labelsPath);
			byte[] data = Files.readAllBytes(path);
			ByteBuffer buffer = ByteBuffer.wrap(data);

			// Ignore first 4-byte int
			buffer.getInt();

			// Read metadata values (4-byte ints)
			int numImages = buffer.getInt();

			// Read labels (1-byte ints)
			for (int imageIndex = 0; imageIndex < numImages; imageIndex++) {
				images.get(imageIndex).label = buffer.get();
			}
		} catch (IOException e) {
			System.out.println("Error reading training labels!");
		}
	}

	public void drawImage() {
		// System.out.println(label);
		BufferedImage bImage = new BufferedImage(rows, cols,
				BufferedImage.TYPE_BYTE_GRAY);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int val = data[r][c];
				int rgb = new Color(val, val, val).getRGB();
				bImage.setRGB(r, c, rgb);
			}
		}
		ImageIcon icon = new ImageIcon();
		icon.setImage(bImage);
		JOptionPane.showMessageDialog(null, icon);
	}

	// Quick example of using this class
	public static void main(String[] args) throws Throwable {
		double minValue = -1, maxValue = 1;
		ArrayList<ImageData> imagesTrain = readData(args[0], args[1]);
		ArrayList<ImageData> imagesTest = readImageFile(args[2]);

		// assign random weights
		weightsLevel1 = generateRandom(weightsLevel1, minValue, maxValue);
		weightsLevel2 = generateRandom(weightsLevel2, minValue, maxValue);
		weightsLevel3 = generateRandom(weightsLevel3, minValue, maxValue);

		// train the image using 30000 images
		System.out.println("Training the system....");
		for (int i = 0; i < imagesTrain.size(); i++) {
			ImageData imageData = imagesTrain.get(i);
			imageData.inputHiddenLayer1 = imageData.generateInputValue(
					imageData.inputNodes, weightsLevel1,
					imageData.inputHiddenLayer1);
			imageData.outputHiddenLayer1 = imageData
					.generateOutputValue(imageData.inputHiddenLayer1);
			imageData.inputHiddenLayer2 = imageData.generateInputValue(
					imageData.outputHiddenLayer1, weightsLevel2,
					imageData.inputHiddenLayer2);
			imageData.outputHiddenLayer2 = imageData
					.generateOutputValue(imageData.inputHiddenLayer2);
			imageData.outputNodesInput = imageData.generateInputValue(
					imageData.outputHiddenLayer2, weightsLevel3,
					imageData.outputNodesInput);
			imageData.outputNodesOutput = imageData
					.generateOutputValue(imageData.outputNodesInput);
			imageData.calculateOutputError();
			imageData.errorHidden2 = imageData.calculateHiddenError(
					weightsLevel3, imageData.outputHiddenLayer2,
					imageData.errorOutput, imageData.errorHidden2);
			imageData.errorHidden1 = imageData.calculateHiddenError(
					weightsLevel2, imageData.outputHiddenLayer1,
					imageData.errorHidden2, imageData.errorHidden1);
			weightsLevel3 = imageData.changeWeight(imageData.errorOutput,
					weightsLevel3, imageData.outputHiddenLayer2);
			weightsLevel2 = imageData.changeWeight(imageData.errorHidden2,
					weightsLevel2, imageData.outputHiddenLayer1);
			weightsLevel1 = imageData.changeWeight(imageData.errorHidden1,
					weightsLevel1, imageData.inputNodes);
		}
		System.out.println("Training completed.");

		int count = 0;
		PrintWriter writer = new PrintWriter(args[3], "UTF-8");
		// test 30000 images using the trained system
		System.out.println("Testing the system against the testing images....");
		for (int i = 0; i < imagesTest.size(); i++) {
			ImageData imageData = imagesTest.get(i);
			imageData.inputHiddenLayer1 = imageData.generateInputValue(
					imageData.inputNodes, weightsLevel1,
					imageData.inputHiddenLayer1);
			imageData.outputHiddenLayer1 = imageData
					.generateOutputValue(imageData.inputHiddenLayer1);
			imageData.inputHiddenLayer2 = imageData.generateInputValue(
					imageData.outputHiddenLayer1, weightsLevel2,
					imageData.inputHiddenLayer2);
			imageData.outputHiddenLayer2 = imageData
					.generateOutputValue(imageData.inputHiddenLayer2);
			imageData.outputNodesInput = imageData.generateInputValue(
					imageData.outputHiddenLayer2, weightsLevel3,
					imageData.outputNodesInput);
			imageData.outputNodesOutput = imageData
					.generateOutputValue(imageData.outputNodesInput);
			int maxIndex = 0;
			for (int j = 0; j < ImageData.noOfOutputNodes; j++) {
				double newnumber = imageData.outputNodesOutput[j];
				if ((newnumber > imageData.outputNodesOutput[maxIndex])) {
					maxIndex = j;
				}
			}
			// write the digit computed into the output text file
			if (maxIndex == imageData.label)
				count++;
			writer.write("" + maxIndex);
			writer.println();
		}
		writer.close();
		System.out.println("Testing completed.");
		System.out.println();
		System.out.println("Number of matched labels: " + count);
		System.out.println("Accuracy obtained : " + ((float) (count / 300.0))
				+ "%");
	}
}
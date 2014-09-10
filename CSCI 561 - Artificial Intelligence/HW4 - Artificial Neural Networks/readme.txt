I have included all my code in 1 file named ImageData.java. 
Kindly change the directory to my folder named Nidhi_Jain_HW4. Place the training and testing images and labels in the directory along sode ImageData.java.

Steps to compile and execute the code on Aludra:
1. 	To compile the code, in cmd type /usr/usc/jdk/1.6.0_23/bin/javac ImageData.java
		1 class file will get generated.
2.	To execute the code, in cmd type /usr/usc/jdk/1.6.0_23/bin/java ImageData train-images.idx3-ubyte train-labels.idx1-ubyte test-images.idx3-ubyte output.txt
		1 output file will get generated in the same folder as the ImageData.java file namely output.txt which contains 30000 digits.

Steps to compile and execute the code on local machine:
1. 	To compile the code, in cmd type javac ImageData.java
		1 class file will get generated.
2.	To execute the code, in cmd type java ImageData train-images.idx3-ubyte train-labels.idx1-ubyte test-images.idx3-ubyte output.txt
		1 output file will get generated in the same folder as the ImageData.java file namely output.txt which contains 30000 digits.

The code gives an accuracy of above 85% on my machine with the train and test images. If in first time, you do not get the desired accuracy, please run the code once or twice more. Also, on my machine the code exceutes in about 3 minutes. Please let me know if it takes more than 10 minutes on your machine.
I am printing the count and accuracy on the console.

If the code runs out of heap space please include VM argument -Xmx2048M while executing the code.

Please Note: JDK 1.6 or higher needed to execute the code.

For any compilation or execution issues, please contact me on +12179745588.
Thank you.
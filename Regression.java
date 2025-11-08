import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Regression {

    int nbrows;
    int nbcols;
    float predictions[];
    float coefficients[];
    float intercept[];
    float covariance;
    float R_square;
    float x_mean;
    float y_mean;
    float label[];
    float Identity_Matrix[][];
    float X[][];
    float tX[][];
    float tXX[][];
    float tXX_inv[][];
    float tXY[];

    public Regression() {
        this.nbrows = 0;
        this.nbcols = 0;
        this.covariance = 0;
        this.R_square = 0;
        this.x_mean = 0;
        this.y_mean = 0;

        System.out.println("Regression object created");

    }

    void readData(String name) {
        File file = new File(name);
        try (Scanner scanner = new Scanner(file)) {
            String[] values = scanner.nextLine().split(" ");
            nbrows = Integer.parseInt(values[0]);
            nbcols = Integer.parseInt(values[1]);
            System.err.println("Number of rows: " + nbrows);
            System.err.println("Number of columns: " + nbcols);
            this.label = new float[nbrows];
            this.X = new float[nbrows][nbcols];
            this.tX = new float[nbcols][nbrows];
            this.tXX = new float[nbcols][nbcols];
            this.tXX_inv = new float[nbcols][nbcols];
            this.tXY = new float[nbcols];
            this.coefficients = new float[nbcols];
            this.predictions = new float[nbrows];
            System.err.println("Reading labels...");
            if (scanner.hasNextLine()) {
                values = scanner.nextLine().split(" ");
                for (int i = 0; i < nbrows; i++) {
                    label[i] = Float.parseFloat(values[i]);
                }
            }
            System.err.println("Reading X...");

            for (int i = 0; i < nbrows; i++) {
                if (scanner.hasNextLine()) {
                    values = scanner.nextLine().split(" ");
                } else {
                    System.err.println("Not enough data rows in the file.");
                    break;
                }

                for (int j = 0; j < nbcols; j++) {
                    this.X[i][j] = Float.parseFloat(values[j]);
                    tX[j][i] = X[i][j];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void displayMatrix() {
        System.out.println("Labels:");
        for (int i = 0; i < nbrows; i++) {
            System.out.println(label[i]);
        }
        System.out.println();

        System.out.println("X:");
        for (int i = 0; i < nbrows; i++) {
            for (int j = 0; j < nbcols; j++) {
                System.out.print(X[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Transposed X:");
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbrows; j++) {
                System.out.print(tX[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("tXX:");
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbcols; j++) {
                System.out.print(tXX[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("tXX_inv:");
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbcols; j++) {
                System.out.print(tXX_inv[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("tXY:");
        for (int i = 0; i < nbcols; i++) {
            System.out.print(tXY[i] + " ");
        }
        System.out.println();
    }

    void transposedMatrix() {
        // Implementation for calculating the regression coefficients
        for (int i = 0; i < nbrows; i++) {
            for (int j = 0; j < nbcols; j++) {
                tX[j][i] = X[i][j];
            }
        }
    }

    void calculate_tXX() {
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbcols; j++) {
                for (int k = 0; k < nbrows; k++) {
                    tXX[i][j] += tX[j][k] * X[k][i];
                }
            }
        }
    }

    void calculate_tXX_inv() {
        // Implementation for calculating the regression coefficients
        // Placeholder for matrix inversion logic
        // Creation of augmented matrix
        float[][] augmentedMatrix = new float[nbcols][2 * nbcols];
        float pivot = 1;
        float factor;
        for (int i = 0; i < nbcols; i++) {
            for (int j = nbcols; j < 2 * nbcols; j++) {
                if (nbcols + i == j)
                    augmentedMatrix[i][j] = 1;
                else
                    augmentedMatrix[i][j] = 0;
            }
            for (int j = 0; j < nbcols; j++) {
                augmentedMatrix[i][j] = tXX[i][j];
            }
        }

        System.out.println("Augmented Matrix:");
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < 2 * nbcols; j++) {
                System.out.print(augmentedMatrix[i][j] + " ");
            }
            System.out.println();
        }

        // Applying Gauss-Jordan Elimination
        for (int i = 0; i < nbcols; i++) {
            pivot = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * nbcols; j++) {
                augmentedMatrix[i][j] = augmentedMatrix[i][j] / pivot;
            }

            for (int k = 0; k < nbcols; k++) {
                if (k > i || k < i) {
                    factor = augmentedMatrix[k][i];

                    for (int l = 0; l < 2 * nbcols; l++) {
                        augmentedMatrix[k][l] -= factor * augmentedMatrix[i][l];
                    }
                }
            }
        }

        System.out.println("Augmented Matrix After Gauss-Jordan Elimination:");
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < 2 * nbcols; j++) {
                System.out.print(augmentedMatrix[i][j] + " ");
            }
            System.out.println();
        }

        // Extracting the inverse matrix
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbcols; j++) {
                tXX_inv[i][j] = augmentedMatrix[i][j + nbcols];
            }
        }
    }

    void calculate_tXY() {
        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbrows; j++) {
                tXY[i] += tX[i][j] * label[j];
            }
        }
    }

    void calculate_coefficients() {
        // Implementation for calculating the regression coefficients

        for (int i = 0; i < nbcols; i++) {
            for (int j = 0; j < nbcols; j++) {
                coefficients[i] += tXX_inv[i][j] * tXY[j];
            }
        }
}

    void calculate_predictions() {
        // Implementation for calculating the regression coefficients
        for (int i = 0; i < nbrows; i++) {
            predictions[i] = 0;
            for (int j = 0; j < nbcols; j++) {
                predictions[i] += X[i][j] * coefficients[j];
            }
        }
    }

    void displayCoefficientsAndPredictions() {
        System.out.println("Coefficients:");
        for (int i = 0; i < nbcols; i++) {
            System.out.println(coefficients[i]);
        }

        System.out.println("Predictions:");
        for (int i = 0; i < nbrows; i++) {
            System.out.println(predictions[i]);
        }
    }

    void calculate_regression() {
        // Implementation for calculating the regression coefficients
        calculate_tXX();
        calculate_tXX_inv();
        calculate_tXY();
        calculate_coefficients();
        calculate_predictions();
        displayCoefficientsAndPredictions();
    }
};

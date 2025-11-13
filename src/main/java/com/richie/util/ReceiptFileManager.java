package com.richie.util;

import com.richie.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptFileManager {
    private static LocalDateTime now = LocalDateTime.now();
    private static final String receiptsDir = "src/main/java/com/richie/receipts";
    private static final DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final DateTimeFormatter displayTimeStamp = DateTimeFormatter.ofPattern("'Date:' yyyy-MM-dd | 'Time:' HH:mm:ss");

    public static void saveReceipt(Order order) {
        File folder = new File(receiptsDir);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = receiptsDir + "/" + now.format(timeStamp) + ".txt";

        try{
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Funkin Poke!!! RECEIPT\n");
            bufferedWriter.write("Date: " + LocalDateTime.now().format(displayTimeStamp) + "\n");
            bufferedWriter.write("======================================\n");

            for(Product p : order.getItems()) {
                bufferedWriter.write(String.format("Item: %s, Price: %.2f\n", p.getName(), p.getPrice()));
                if(p instanceof PokeBowl) {
                    PokeBowl s = (PokeBowl) p;
                    bufferedWriter.write(" Base: " + s.getBase() + " | Size: " + s.getSize() + "\n");
                    if(!s.getToppings().isEmpty()) {
                        bufferedWriter.write("  Toppings:\n");
                        for(Topping t : s.getToppings()) {
                            bufferedWriter.write("   - " + t.getName() + "\n");
                        }
                    }
                    // ADD THIS BLOCK FOR EXTRAS
                    if(s.hasExtras()) {
                        bufferedWriter.write("  Extras:\n");
                        for(Extra e : s.getExtras()) {
                            bufferedWriter.write(String.format("   - %s: +$%.2f\n", e.getName(), e.getUpcharge()));
                        }
                    }
                } else if (p instanceof Drink) {
                    Drink d = (Drink) p;
                    bufferedWriter.write(" Size: " + d.getSize() + " | Flavor: " + d.getFlavor());
                    bufferedWriter.write("\n");
                } else if (p instanceof Sides) {
                    Sides s = (Sides) p;
                    bufferedWriter.write(" Type: " + s.getType() + "\n");
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.write("======================================\n");
            bufferedWriter.write(String.format("Subtotal:                $%.2f\n", order.getSubtotal()));
            bufferedWriter.write(String.format("Tax (7%%):                $%.2f\n", order.getTax()));
            bufferedWriter.write("--------------------------------------\n");
            bufferedWriter.write(String.format("TOTAL:                   $%.2f\n", order.getTotal()));
            bufferedWriter.write("======================================\n");
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Receipt saved: " + receiptsDir);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void previewReceipt(Order order) {
        System.out.println("\n======================================");
        System.out.println("              ORDER SUMMARY             ");
        System.out.println("======================================");

        for(Product p : order.getItems()) {
            System.out.printf("\nItem: %s, Price: $%.2f\n", p.getName(), p.getPrice());

            if(p instanceof PokeBowl) {
                PokeBowl pokeBowl = (PokeBowl) p;
                System.out.println(" Base: " + pokeBowl.getBase() + " | Size: " + pokeBowl.getSize());

                if(!pokeBowl.getToppings().isEmpty()) {
                    System.out.println("  Toppings:");
                    for(Topping t : pokeBowl.getToppings()) {
                        System.out.println("   - " + t.getName());
                    }
                }

                if(pokeBowl.hasExtras()) {
                    System.out.println("  Extras:");
                    for(Extra extra : pokeBowl.getExtras()) {
                        System.out.printf("   - %s: +$%.2f\n", extra.getName(), extra.getUpcharge());
                    }
                }

            } else if (p instanceof Drink) {
                Drink d = (Drink) p;
                System.out.println(" Size: " + d.getSize() + " | Flavor: " + d.getFlavor());

            } else if (p instanceof Sides) {
                Sides s = (Sides) p;
                System.out.println(" Type: " + s.getType());
            }
        }

        System.out.println("\n======================================");
        System.out.printf("Subtotal:                 $%.2f\n", order.getSubtotal());
        System.out.printf("Tax (7%%):                 $%.2f\n", order.getTax());
        System.out.println("--------------------------------------");
        System.out.printf("TOTAL:                    $%.2f\n", order.getTotal());
        System.out.println("======================================\n");
    }

}
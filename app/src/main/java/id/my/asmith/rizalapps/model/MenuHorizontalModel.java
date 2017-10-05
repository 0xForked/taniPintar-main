package id.my.asmith.rizalapps.model;

/**
 * Created by Asmith on 9/20/2017.
 */

public class MenuHorizontalModel {

        // Getter and Setter model for recycler view items
        private String title;
        private int image;

        public MenuHorizontalModel(String title, int image) {

            this.title = title;

            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public int getImage() {
            return image;
        }
}


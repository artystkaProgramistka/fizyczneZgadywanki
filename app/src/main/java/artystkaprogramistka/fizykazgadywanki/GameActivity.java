package artystkaprogramistka.fizykazgadywanki;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements
        View.OnTouchListener, GestureDetector.OnGestureListener {

    private TextView secondsRemained;
    private TextView haslo;
    private String[] puzzlesArray;
    private short playerPoints = 0;
    private LinearLayout buttons;
    private ImageView btnNo;
    private ImageView btnYes;
    private ImageView b;
    private boolean isPaused = false; // 0 - player can click pause, 1 - player can click continue
    private GestureDetector gestureDetector;
    private CountDownTimerPausable timer = null;
    private int screenX;


    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttons = findViewById(R.id.buttons);

        buttons.setOnTouchListener(this);

        gestureDetector = new GestureDetector(this, this);

        secondsRemained = findViewById(R.id.time);
        secondsRemained.setOnTouchListener(this);

        haslo = findViewById(R.id.textView);

        btnNo = findViewById(R.id.no);
        btnNo.setOnTouchListener(this);

        btnYes = findViewById(R.id.yes);
        btnYes.setOnTouchListener(this);

        b = findViewById(R.id.b);
        b.setOnTouchListener(this);

        timer =  new CountDownTimerPausable(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsRemained.setText("Przyłóż telefon do czoła!");
                haslo.setText("Gra rozpocznie się za: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                btnNo.setVisibility(View.VISIBLE);
                btnYes.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);

                secondsRemained.setText("Już!");

                play();

                timer =  new CountDownTimerPausable(300000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        secondsRemained.setText("Tyle pozostało Ci sekund do końca rozgrywki: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        secondsRemained.setText("Czas się skończył!");
                        stopPlayer();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this); //there was a problem with this line
                        alertDialogBuilder.setMessage("GameOver!\nZdobyte punkty: " + playerPoints)
                                .setCancelable(false)
                                .setPositiveButton("Zagraj jeszcze raz", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).setNegativeButton("Wyjdź do menu głównego", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }.start();

                // Get a Display object to access screen details
                Display display = getWindowManager().getDefaultDisplay();
                // Load the resolution into a Point object
                Point size = new Point();
                display.getSize(size);

                screenX = size.x;

                loadPuzzlesArray();
                haslo.setText(randomPuzzleToGuess());
            }
        }.start();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.buttons) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        else if(v.getId() == R.id.time) {
            haslo.setText(randomPuzzleToGuess());
        }return false;
    }

    private void playerHasGivenUp(){
        playerPoints--;
        haslo.setText(randomPuzzleToGuess());
    }

    private void playerHasGuessed(){
        playerPoints++;
        haslo.setText(randomPuzzleToGuess());
    }

    private void loadPuzzlesArray(){
        puzzlesArray = new String[]{
                "Bozon Higgsa", "Praca wyjścia", "Energia mechaniczna", "Energia potencjalna grawitacji", "Jednostka energii", "Inercja", "Układ nieinercjalny", "Tarcie toczne i poślizgowe", "Prawo Pascala", "Prawo Bernoullego", "prędkość ponaddźwiekowa", "magnes neodymowy", "wypalone paliwo jądrowe", "przekroczenie horyzontu zdarzeń", "supermasywna czarna dziura", "wybuch supernowej", "gwiazda neutronowa", "cząstka fundamentalna", "wektory i tensory", "zasada zachowania energii", "E = m c^2", "ogólna teoria względności", "Teoria strun", "czwarty wymiar", "powrócić z horyzontu zdarzeń", "Most Einsteina-Rossena", "akcelerator cząstek", "szybciej niż światło", "prędkość ponaddźwiękowa", "perpetuum mobile", "sztuczna grawitacja", "rozpad alfa", "zderzenia cząstek", "promieniowanie kosmiczne", "ujemna materia", "ciemna energia", "ciemna materia", "cząstki elementarne", "leptony", "spin elektronu", "kolor elektronu", "model standardowy", "dynamika Newtona", "boska czastka", "pole Higgsa", "kot Shrodingera", "Caltech", "zderzacz hadronów", "Przyszłość ludzkości", "Wszechświaty równoległe", "hipoteza Hawkinga", "Fizyka rzeczy niemozliwych", "Hiperprzestrzeń", "Czarne dziury nie mają włosów", "Burza w szklance wody", "Fizyka rzeczy niemożliwych", "amplituda drgań", "przemiana adiabatyczna", "barwa dźwięku", "Most Einsteina-Rossena", "cykl Carnota", "druga predkość kosmiczna", "dualizm korpuskularno-falowy", "zjawisko dyfuzji", "dyfrakcja i interferencja", "indukcyjnośc", "równania Maxwella", "mechanika Kwantowa", "marmur i drewno", "entropia", "plamy słoneczne", "rad i polon", "skaczący elektron", "podczerwień", "promieniowanie X", "punkt krytyczny", "równowaga termodynamiczna", "sublimacja i resublimacja", "rezonans", "lampa elektronowa", "klatka Faradaya", "liczba Avogadra", "spadek swobodny", "siła Lorentza", "suchy lód", "ciekły azot", "stan nadciekły", "nadprzewodnik", "półprzewodnik", "tablica Mendelejewa", "warunki normalne", "widmo fal elektromagnetycznych", "zorza polarna", "zerowa zasada termodynamiki", "zjawisko Dopplera", "katoda i anoda", "ogniwo galwaniczne", "bateria słoneczna", "butelka lejdejska", "schwtać piorun", "bimetal", "ciekły kryształ", "ciężka woda", "cyklotron", "czoło fali", "datowanie węglem radioaktywnym", "diamagnetyk", "dielektryk", "dzura ozonowa", "fizyka relatywistyczna", "Galileusz, Galileo Galilei", "gaz doskonały", "model heliocentryczny", "jednostka astronomiczna", "rok świetlny", "Mikołaj Kopernik", "kwarki", "Isaan Newton", "Max Planck", "spaghettizacja", "zakócenia czasu", "fale grawitacyjne", "zakłócenia czasoprzestrzeni", "Flatlandia", "ukryte wymiary", "Wielki wybuch", "ciała niebieskie", "droga mleczna", "Maria Skłodowska-Curie", "Neils Bohr", "James Clerk Maxwell", "Nikola Tesla", "Teleskop Hubble'a", "zdjęcie czarnej dziury", "Sagittarius A*", "Andromeda", "Proxima Centauri", "widzialny wszechświat", "środek widzialnego wszechświata", "prawo Hubble'a", "przestrzeń międzygalaktyczna", "burze słoneczne", "rozbłyski słoneczne", "baza na Marsie", "Koniec Kosmosu", "baza na Księżycu", "lot bezzałogowy", "łazik na marsie", "zerwanie czasoprzestrzeni", "stopnie rozwoju cywilizacji", "paradoks Fermiego", "oszukac prawa fizyki", "aphelium i peryhelium", "bozony i fermiony", "pole siłowe", "teleportacja", "Michael Faraday", "cztery siły", "plazma", "antycząstka", "wyciek powietrza", "Star Trek", "lewitacja magnetyczna", "nanorurki węglowe", "winda kosmiczna", "międzynarodowa stacja kosmiczna", "nadprzewodnik działający w temperaturze pokojowej", "efekt Meissnera", "nanotechnologia", "hologram", "broń laserowa", "rewolucja kwantowa", "masery i lasery", "przeskok kwantowy", "tunelowanie", "zasada nieoznaczoności Heisenberga", "Bóg nie gra w kości",  "paralaksa", "skoki kwantowe", "wiek Wszechświata", "Wszechświat w skorupce od orzecha", "Krótka historia czasu", "koherencja", "splątanie kwantowe", "nieokreślony stan", "parowanie czarnej dziury", "komputery kwantowe", "superpozycja", "qubit", "punkt Curie", "stan nadprzewodnictwa",  "prawo Moore'a", "telepatia", "wykrywacz kłamstw", "psychokineza", "nanoboty", "LIGO", "istoty pozaziemskie", "żagiel słoneczny", "antysymetria", "supersymetria", "rakieta na antymaterię", "antywszechświat", "tunel czasoprzestrzenny", "podróże w czasie", "Multiwszechświat", "Wszechświat potomny", "ewolucja Wszechświata", "Rozkład próżni", "Fale grawitacyjne", "Promieniowanie kosmiczne", "Prawo Coulomba", "Ruch jednostajnie przyspieszony", "Zderzenie sprężyste", "Mózg Boltzmana", "Demon Maxwella", "Równanie Schrodingera", "Studnia grawitacyjna", "Materiały rozszczepialne", "Siatka dyfrakcyjna", "Naczynia połączone", "Ciecz nienewtonowska", "Przepływ laminarny", "Energia z próżni", "prekognicja"};

    }

    private String randomPuzzleToGuess(){

        Random generator = new Random();
        short randomNumber = (short) generator.nextInt(puzzlesArray.length - 1);
        return puzzlesArray[randomNumber];
    }


    private void play() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gamesong);
        }
        mediaPlayer.start();
        timer.start();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayer();
        timer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        play();
        timer.start();

    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (event.getX() > 2 * (screenX / 3)){
            //zgadłem
            playerHasGuessed();
        }
        else if (event.getX() < screenX / 3){
            //pasuję
            playerHasGivenUp();
        }
        else {
            if(isPaused) {
                timer.start();
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                b.setImageResource(R.drawable.pause);
            }else {
                timer.pause();
                mediaPlayer.pause();
                b.setImageResource(R.drawable.start);
            }
            isPaused = !isPaused;
            return true;
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

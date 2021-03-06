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
                secondsRemained.setText("Przy?????? telefon do czo??a!");
                haslo.setText("Gra rozpocznie si?? za: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                btnNo.setVisibility(View.VISIBLE);
                btnYes.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);

                secondsRemained.setText("Ju??!");

                play();

                timer =  new CountDownTimerPausable(300000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        secondsRemained.setText("Tyle pozosta??o Ci sekund do ko??ca rozgrywki: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        secondsRemained.setText("Czas si?? sko??czy??!");
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
                                }).setNegativeButton("Wyjd?? do menu g????wnego", new DialogInterface.OnClickListener() {
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
                "Bozon Higgsa", "Praca wyj??cia", "Energia mechaniczna", "Energia potencjalna grawitacji", "Jednostka energii", "Inercja", "Uk??ad nieinercjalny", "Tarcie toczne i po??lizgowe", "Prawo Pascala", "Prawo Bernoullego", "pr??dko???? ponadd??wiekowa", "magnes neodymowy", "wypalone paliwo j??drowe", "przekroczenie horyzontu zdarze??", "supermasywna czarna dziura", "wybuch supernowej", "gwiazda neutronowa", "cz??stka fundamentalna", "wektory i tensory", "zasada zachowania energii", "E = m c^2", "og??lna teoria wzgl??dno??ci", "Teoria strun", "czwarty wymiar", "powr??ci?? z horyzontu zdarze??", "Most Einsteina-Rossena", "akcelerator cz??stek", "szybciej ni?? ??wiat??o", "pr??dko???? ponadd??wi??kowa", "perpetuum mobile", "sztuczna grawitacja", "rozpad alfa", "zderzenia cz??stek", "promieniowanie kosmiczne", "ujemna materia", "ciemna energia", "ciemna materia", "cz??stki elementarne", "leptony", "spin elektronu", "kolor elektronu", "model standardowy", "dynamika Newtona", "boska czastka", "pole Higgsa", "kot Shrodingera", "Caltech", "zderzacz hadron??w", "Przysz??o???? ludzko??ci", "Wszech??wiaty r??wnoleg??e", "hipoteza Hawkinga", "Fizyka rzeczy niemozliwych", "Hiperprzestrze??", "Czarne dziury nie maj?? w??os??w", "Burza w szklance wody", "Fizyka rzeczy niemo??liwych", "amplituda drga??", "przemiana adiabatyczna", "barwa d??wi??ku", "Most Einsteina-Rossena", "cykl Carnota", "druga predko???? kosmiczna", "dualizm korpuskularno-falowy", "zjawisko dyfuzji", "dyfrakcja i interferencja", "indukcyjno??c", "r??wnania Maxwella", "mechanika Kwantowa", "marmur i drewno", "entropia", "plamy s??oneczne", "rad i polon", "skacz??cy elektron", "podczerwie??", "promieniowanie X", "punkt krytyczny", "r??wnowaga termodynamiczna", "sublimacja i resublimacja", "rezonans", "lampa elektronowa", "klatka Faradaya", "liczba Avogadra", "spadek swobodny", "si??a Lorentza", "suchy l??d", "ciek??y azot", "stan nadciek??y", "nadprzewodnik", "p????przewodnik", "tablica Mendelejewa", "warunki normalne", "widmo fal elektromagnetycznych", "zorza polarna", "zerowa zasada termodynamiki", "zjawisko Dopplera", "katoda i anoda", "ogniwo galwaniczne", "bateria s??oneczna", "butelka lejdejska", "schwta?? piorun", "bimetal", "ciek??y kryszta??", "ci????ka woda", "cyklotron", "czo??o fali", "datowanie w??glem radioaktywnym", "diamagnetyk", "dielektryk", "dzura ozonowa", "fizyka relatywistyczna", "Galileusz, Galileo Galilei", "gaz doskona??y", "model heliocentryczny", "jednostka astronomiczna", "rok ??wietlny", "Miko??aj Kopernik", "kwarki", "Isaan Newton", "Max Planck", "spaghettizacja", "zak??cenia czasu", "fale grawitacyjne", "zak????cenia czasoprzestrzeni", "Flatlandia", "ukryte wymiary", "Wielki wybuch", "cia??a niebieskie", "droga mleczna", "Maria Sk??odowska-Curie", "Neils Bohr", "James Clerk Maxwell", "Nikola Tesla", "Teleskop Hubble'a", "zdj??cie czarnej dziury", "Sagittarius A*", "Andromeda", "Proxima Centauri", "widzialny wszech??wiat", "??rodek widzialnego wszech??wiata", "prawo Hubble'a", "przestrze?? mi??dzygalaktyczna", "burze s??oneczne", "rozb??yski s??oneczne", "baza na Marsie", "Koniec Kosmosu", "baza na Ksi????ycu", "lot bezza??ogowy", "??azik na marsie", "zerwanie czasoprzestrzeni", "stopnie rozwoju cywilizacji", "paradoks Fermiego", "oszukac prawa fizyki", "aphelium i peryhelium", "bozony i fermiony", "pole si??owe", "teleportacja", "Michael Faraday", "cztery si??y", "plazma", "antycz??stka", "wyciek powietrza", "Star Trek", "lewitacja magnetyczna", "nanorurki w??glowe", "winda kosmiczna", "mi??dzynarodowa stacja kosmiczna", "nadprzewodnik dzia??aj??cy w temperaturze pokojowej", "efekt Meissnera", "nanotechnologia", "hologram", "bro?? laserowa", "rewolucja kwantowa", "masery i lasery", "przeskok kwantowy", "tunelowanie", "zasada nieoznaczono??ci Heisenberga", "B??g nie gra w ko??ci",  "paralaksa", "skoki kwantowe", "wiek Wszech??wiata", "Wszech??wiat w skorupce od orzecha", "Kr??tka historia czasu", "koherencja", "spl??tanie kwantowe", "nieokre??lony stan", "parowanie czarnej dziury", "komputery kwantowe", "superpozycja", "qubit", "punkt Curie", "stan nadprzewodnictwa",  "prawo Moore'a", "telepatia", "wykrywacz k??amstw", "psychokineza", "nanoboty", "LIGO", "istoty pozaziemskie", "??agiel s??oneczny", "antysymetria", "supersymetria", "rakieta na antymateri??", "antywszech??wiat", "tunel czasoprzestrzenny", "podr????e w czasie", "Multiwszech??wiat", "Wszech??wiat potomny", "ewolucja Wszech??wiata", "Rozk??ad pr????ni", "Fale grawitacyjne", "Promieniowanie kosmiczne", "Prawo Coulomba", "Ruch jednostajnie przyspieszony", "Zderzenie spr????yste", "M??zg Boltzmana", "Demon Maxwella", "R??wnanie Schrodingera", "Studnia grawitacyjna", "Materia??y rozszczepialne", "Siatka dyfrakcyjna", "Naczynia po????czone", "Ciecz nienewtonowska", "Przep??yw laminarny", "Energia z pr????ni", "prekognicja"};

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
            //zgad??em
            playerHasGuessed();
        }
        else if (event.getX() < screenX / 3){
            //pasuj??
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

package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class MyGdxGame extends ApplicationAdapter implements MidiProcess  {
	SpriteBatch batch;
	Texture img;
	MidiProcess midiProcess;

	int bluec = 0;
	String manufacturer;
	String MidiMsg;
	private Rectangle note;
	private ArrayList<Rectangle> notes;
	private OrthographicCamera camera;
	private Texture noteTexture;
	private World world;
	private Stage stage;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private HashMap<String, MyPointLight> lightHashMap = new HashMap<String,MyPointLight>();
	private ArrayList<MyPointLight> lightFadingList = new ArrayList<>();
	private Random random = new Random();
	private NotesColors notesColors = new NotesColors();
	private boolean sustainIsHeld = false;
	private final Pool<MyPointLight> myPointLightPool = new Pool<MyPointLight>() {
		@Override
		protected MyPointLight newObject() {
			return new MyPointLight(rayHandler, 64);
		}
	};
	//Animation
	private TextureAtlas textureAtlas;

	private Animation<TextureRegion> animation;
	private ArrayList<Animation<TextureRegion>> animations = new ArrayList<>();
	private boolean doRender = true;
	private ArrayList<Particles> particlesList  = new ArrayList<>();
	private ArrayList<NoteEvent> notesPlayed = new ArrayList<>();
	private int noteCount;

	public  MyGdxGame(){

	}
	@Override
	public void create () {
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/var1/particles1.txt"));
		animation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(animation);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/var2/particles2.txt"));
		animation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(animation);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/var3/particles3.txt"));
		animation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(animation);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/var4/particles4.txt"));
		animation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(animation);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/var5/particles5.txt"));
		animation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(animation);

		noteTexture = new Texture(Gdx.files.internal("note.png"));

		BitmapFont font = new BitmapFont(Gdx.files.internal("default.fnt"));
		noteTexture = new Texture(Gdx.files.internal("badlogic.jpg"));

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		world = new World(new Vector2(0, 0), true);



		rayHandler = new RayHandler(world);

		batch = new SpriteBatch();
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE); // Additive Mode

		Gdx.input.setInputProcessor(stage);
		float ratio = (float)(Gdx.graphics.getWidth())/(float)(Gdx.graphics.getHeight());
		stage = new Stage(new ScreenViewport());
		stage.getCamera().position.set(0,0,10);
		stage.getCamera().lookAt(0,0,0);
		stage.getCamera().viewportWidth = 10;
		stage.getCamera().viewportHeight = 10/ratio;
		debugRenderer = new Box2DDebugRenderer();
		new WindowsFrame(world, stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);
		Gdx.app.debug("WORLD STUFF", String.valueOf(Gdx.graphics.getWidth()));

		rayHandler.setBlurNum(3);



		rayHandler.setShadows(false);


		notes = new ArrayList<>();


	}






	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		stage.draw();

		rayHandler.setCombinedMatrix(stage.getCamera().combined,0,0,1,1);
		try {
			rayHandler.updateAndRender();
		} catch (Exception e) {
			e.printStackTrace();
		}

		batch.begin();

		for(Iterator<NoteEvent> notesIterator = notesPlayed.iterator(); notesIterator.hasNext();){
			if(noteCount==4){
				noteCount = 0;
			}
			else{
				noteCount +=1;
			}

			NoteEvent note = notesIterator.next();
			float[] colorvals = notesColors.noteValueMap.get(note.getNoteName());
			float alphavalue = (float) (Math.sqrt(note.getVelocity())/11.27);

			Particles particle = new Particles(0f,new Color(colorvals[0]/255f, colorvals[1]/255f, colorvals[2]/255f, alphavalue),
					(Gdx.graphics.getWidth()/88f)*note.noteValue-512, 0, noteCount);

			particlesList.add(particle);
			notesIterator.remove();

		}
		for(Iterator<Particles> timeit = particlesList.iterator(); timeit.hasNext();){
			Particles curParticles = timeit.next();
			TextureRegion frame = animations.get(curParticles.count).getKeyFrame(curParticles.elapsedTime, false);
			batch.setColor(curParticles.color);
			batch.draw(frame,curParticles.x,0);
			curParticles.elapsedTime += Gdx.graphics.getDeltaTime();
			Gdx.app.debug("PARTICLESVALUES", String.valueOf(curParticles.x) + "ElapsedTime: " + curParticles.elapsedTime);
			if(curParticles.elapsedTime>2.7){
				timeit.remove();
			}
		}




		for(Iterator<MyPointLight> lightIterator = lightFadingList.iterator(); lightIterator.hasNext();){
				MyPointLight fadingLight = lightIterator.next();

				fadingLight.setDistance(fadingLight.getDistance() - 8 * Gdx.graphics.getDeltaTime());
				if (fadingLight.getDistance() <= 0) {
					fadingLight.setDistance(0);
					myPointLightPool.free(fadingLight);
					lightIterator.remove();
				}


		};


		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		noteTexture.dispose();
		rayHandler.dispose();
		textureAtlas.dispose();
	}

	@Override
	public void onNoteReceived(String midiMsg) {
		this.MidiMsg = midiMsg;
		NoteEvent noteEvent = new NoteEvent(midiMsg);
		Gdx.app.debug("MIDINOTE", noteEvent.getNoteType() + ", " + noteEvent.getNoteName());
		if(noteEvent.getNoteType().equals("NoteOn")) {

			float alphavalue = (float) (Math.sqrt(noteEvent.getVelocity())/11.27);
			float distanceval =  (float)(Math.sqrt(noteEvent.getVelocity())/11.27)*2;
			MyPointLight pl = myPointLightPool.obtain();
			pl.setPosition((float)random.nextInt(10)-5,(float)random.nextInt(4)-2);
			pl.setDistance(distanceval);
			float[] rgbvals = notesColors.noteValueMap.get(noteEvent.getNoteName());
			notesPlayed.add(noteEvent);
			pl.setColor(new Color(rgbvals[0]/255f, rgbvals[1]/255f, rgbvals[2]/255f, alphavalue));
			lightHashMap.put(noteEvent.getNoteName().concat(String.valueOf(noteEvent.getOctave())), pl);

		}
		else if(noteEvent.getNoteType().equals("NoteOff")){
			MyPointLight matchingLight = lightHashMap.get(noteEvent.getNoteName().concat(String.valueOf(noteEvent.getOctave())));
			if(sustainIsHeld){
				lightFadingList.add(matchingLight);

			}
			else{
				matchingLight.setDistance(0);
				myPointLightPool.free(matchingLight);
			}



		}
		else if(noteEvent.getNoteType().equals("Control")){
			sustainIsHeld = true;
		}
		else{
			sustainIsHeld = false;
//			for(Iterator<MyPointLight> lightIterator = lightFadingList.iterator(); lightIterator.hasNext();){
//				MyPointLight fadingLight = lightIterator.next();
//				fadingLight.setDistance(0);
//				myPointLightPool.free(fadingLight);
//				lightIterator.remove();
//			};
		}

	}

	@Override
	public void deviceAdded(String manufacturer) {
		this.manufacturer = manufacturer;
	}
}

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
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
	private OrthographicCamera camera;
	private Texture noteTexture;
	private Texture bgTexture;
	private World world;
	private Stage stage;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private HashMap<String, MyPointLight> lightHashMap = new HashMap<String,MyPointLight>();
	private HashMap<String, Particles> partHashMap = new HashMap<String,Particles>();
	private HashMap<String, MyConeLight> coneLightHashMap = new HashMap<String,MyConeLight>();

	private ArrayList<MyPointLight> lightFadingList = new ArrayList<>();
	private ArrayList<MyConeLight> coneFadingList = new ArrayList<>();
	private Random random = new Random();
	private NotesColors notesColors = new NotesColors();
	private boolean sustainIsHeld = false;
	private final Pool<MyPointLight> myPointLightPool = new Pool<MyPointLight>() {
		@Override
		protected MyPointLight newObject() {
			return new MyPointLight(rayHandler, 32);
		}
	};
	private final Pool<MyConeLight> myConeLightPool = new Pool<MyConeLight>() {
		@Override
		protected MyConeLight newObject() {
			return new MyConeLight(rayHandler, 32, null, 0, 0,0, 90, 10);
		}
	};

	//Animation
	private TextureAtlas textureAtlas;

	private Animation<TextureRegion> animation;
	private Animation<TextureRegion> pedalDownAnimation;
	private Animation<TextureRegion> pedalHeldAnimation;
	private ArrayList<Animation<TextureRegion>> animations = new ArrayList<>();
	private boolean doRender = true;
	private ArrayList<Particles> particlesList  = new ArrayList<>();
	private ArrayList<Particles> particlesFadeList  = new ArrayList<>();
	private ArrayList<NoteEvent> notesWaiting = new ArrayList<>();
	private ArrayList<NoteEvent> notesPlayed = new ArrayList<>();
	private ArrayList<Particles> clefLighting = new ArrayList<>();
	private ArrayList<Particles> clefLits = new ArrayList<>();

	private int noteCount;
	private ArrayList<NoteEvent> noteOffList = new ArrayList<>();
	private Color sustainColor = new Color(0,0,0, 1);
	private ShapeRenderer shapeRenderer;
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
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/litingsheet/liting.txt"));
		pedalDownAnimation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/litsheet/lit.txt"));
		pedalHeldAnimation = new Animation<TextureRegion>(1f/30f, textureAtlas.getRegions());
		animations.add(pedalDownAnimation);
		animations.add(pedalHeldAnimation);
		noteTexture = new Texture(Gdx.files.internal("note.png"));
		bgTexture = new Texture(Gdx.files.internal("bg.png"));
		BitmapFont font = new BitmapFont(Gdx.files.internal("default.fnt"));

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		world = new World(new Vector2(0, 0), true);
		rayHandler = new RayHandler(world);

		batch = new SpriteBatch();
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_FUNC_ADD);
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
		rayHandler.setAmbientLight(0.1f,0.1f,0.1f,1f);
		rayHandler.setShadows(false);
	}

	@Override

	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		stage.draw();

		rayHandler.setCombinedMatrix(stage.getCamera().combined, 0, 0, 1, 1);
		try {
			rayHandler.updateAndRender();
		} catch (Exception e) {
			e.printStackTrace();
		}

		batch.begin();

		//ITERATE THROUGH NOTES ARRAY
		notesPlayed.addAll(notesWaiting);
		notesWaiting.clear();

		for (Iterator<NoteEvent> notesIterator = notesPlayed.iterator(); notesIterator.hasNext(); ) {
			//GO THROUGH 5 ANIMATION CHOICES
			if (noteCount == animations.size()-3) {
				noteCount = 0;
			} else {
				noteCount += 1;
			}


			NoteEvent note = notesIterator.next();
			if (note.getNoteType().equals("NoteOn")) {

				float[] colorvals = notesColors.noteValueMap.get(note.getNoteName());
				float alphavalue = (float) (Math.sqrt(note.getVelocity()) / 11.27);
				//---------------------------Particle class creation for graphics use-----------------------------------
				Particles particle = new Particles(0f, new Color(colorvals[0] / 255f, colorvals[1] / 255f, colorvals[2] / 255f, 1),
						(Gdx.graphics.getWidth() / 88f) * note.noteValue - 512, 0, noteCount);
				particle.r = colorvals[0] / 255f;
				particle.g = colorvals[1] / 255f;
				particle.b = colorvals[2] / 255f;
				particle.a = 1;
				sustainColor = new Color(particle.r, particle.g, particle.b, 1);
				particle.noteID = note.getNoteName().concat(String.valueOf(note.getOctave()));
				partHashMap.put(note.getNoteName().concat(String.valueOf(note.getOctave())), particle);
//				particlesList.add(particle);

				//--------------------------------------------------------------------------------------------------------

				//DRAW A POINT LIGHT AND ADD IT TO LIST
				float distanceval = (float) (Math.sqrt(note.getVelocity()) / 11.27) * 3;
				MyPointLight pl = myPointLightPool.obtain();
				pl.setPosition((float) random.nextInt(10) - 5, (float) random.nextInt(4) - 2);
				pl.setDistance(distanceval);
				float[] rgbvals = notesColors.noteValueMap.get(note.getNoteName());
				pl.setColor(new Color(rgbvals[0] / 255f, rgbvals[1] / 255f, rgbvals[2] / 255f, alphavalue));
				lightHashMap.put(note.getNoteName().concat(String.valueOf(note.getOctave())), pl);
				notesIterator.remove();
				MyConeLight cl = myConeLightPool.obtain();
				cl.setPosition((10 / 88f) * note.noteValue-5, -2.7f);
				cl.setDistance(5);
				cl.setColor(Color.WHITE);
				coneLightHashMap.put(note.getNoteName().concat(String.valueOf(note.getOctave())), cl);
			} else if (note.getNoteType().equals("NoteOff")) {

				MyPointLight matchingLight = lightHashMap.get(note.getNoteName().concat(String.valueOf(note.getOctave())));
				MyConeLight matchingCone = coneLightHashMap.get(note.getNoteName().concat(String.valueOf(note.getOctave())));
				Particles matchingParticles = partHashMap.get(note.getNoteName().concat(String.valueOf(note.getOctave())));
				partHashMap.remove(note.getNoteName().concat(String.valueOf(note.getOctave())));

				if (sustainIsHeld) {
					lightFadingList.add(matchingLight);
					coneFadingList.add(matchingCone);
					particlesList.add(matchingParticles);

				} else {
					particlesFadeList.add(matchingParticles);

					matchingCone.setDistance(0);
					myConeLightPool.free(matchingCone);
					matchingLight.setDistance(0);
					myPointLightPool.free(matchingLight);
				}
				notesIterator.remove();

			} else if (note.getNoteType().equals("Control")) {
				sustainIsHeld = true;
				Particles clefLight = new Particles(0f, sustainColor, Gdx.graphics.getWidth()/2f - 256, Gdx.graphics.getHeight()/2f, 5);
				clefLight.animType = 1;
				particlesList.add(clefLight);
				notesIterator.remove();

			} else {
				sustainIsHeld = false;
				for (Iterator<MyPointLight> lightIterator = lightFadingList.iterator(); lightIterator.hasNext(); ) {
					MyPointLight fadingLight = lightIterator.next();
					fadingLight.setDistance(0);
					myPointLightPool.free(fadingLight);
					lightIterator.remove();
				}

				particlesFadeList.addAll(particlesList);
				particlesList.clear();
				notesIterator.remove();
			}

		}

		for (Iterator<Particles> susIt = clefLits.iterator(); susIt.hasNext(); ) {
			Particles curParticles = susIt.next();
			try {
				TextureRegion frame = pedalHeldAnimation.getKeyFrame(curParticles.elapsedTime, true);
				batch.setColor(curParticles.color);
				batch.draw(frame, curParticles.x, curParticles.y);
				curParticles.elapsedTime += Gdx.graphics.getDeltaTime();

			}
			catch (Exception e){
				continue;
			}

		}
		for (Iterator timeit = partHashMap.entrySet().iterator(); timeit.hasNext(); ) {
			Map.Entry curEntry = (Map.Entry) timeit.next();
			Particles curParticles = (Particles) curEntry.getValue();
				TextureRegion frame = animations.get(curParticles.count).getKeyFrame(curParticles.elapsedTime, false);

				batch.setColor(curParticles.color);
				batch.draw(frame, curParticles.x, 0);
				curParticles.elapsedTime += Gdx.graphics.getDeltaTime();

		}

		for (Iterator<Particles> susIt = particlesList.iterator(); susIt.hasNext(); ) {
			Particles curParticles = susIt.next();
			boolean loop = curParticles.animType == 2;
			try {
				TextureRegion frame = animations.get(curParticles.count).getKeyFrame(curParticles.elapsedTime, loop);
				batch.setColor(curParticles.color);
				batch.draw(frame, curParticles.x, 0);
				curParticles.elapsedTime += Gdx.graphics.getDeltaTime();
				if (animations.get(curParticles.count).isAnimationFinished(curParticles.elapsedTime)&& !loop)  {
					if(curParticles.animType == 1){
						Particles heldParticles = new Particles(0f, curParticles.color, curParticles.x, curParticles.y, 6);
						heldParticles.animType = 2;
						clefLits.add(heldParticles);
					}
					susIt.remove();
				}
			}

			catch (Exception e){
				continue;
			}

		}
		particlesList.addAll(clefLits);
		clefLits.clear();
		for (Iterator<Particles> susIt = particlesFadeList.iterator(); susIt.hasNext(); ) {
			try {
				Particles curParticles = susIt.next();
				TextureRegion frame = animations.get(curParticles.count).getKeyFrame(curParticles.elapsedTime, false);
				curParticles.a -= 2 * Gdx.graphics.getDeltaTime();
				batch.setColor(new Color(curParticles.r, curParticles.g, curParticles.b, curParticles.a));
				batch.draw(frame, curParticles.x, curParticles.y);

				curParticles.elapsedTime += Gdx.graphics.getDeltaTime();
				if (curParticles.a <= 0) {
					susIt.remove();
				}
			}
			catch (Exception e){
				continue;
			}
		}



		for(Iterator<MyPointLight> lightIterator = lightFadingList.iterator(); lightIterator.hasNext();){
				MyPointLight fadingLight = lightIterator.next();

				fadingLight.setDistance(fadingLight.getDistance() - 4 * Gdx.graphics.getDeltaTime());
				if (fadingLight.getDistance() <= 0) {
					fadingLight.setDistance(0);
					myPointLightPool.free(fadingLight);
					lightIterator.remove();
				}
		};
		for(Iterator<MyConeLight> lightIterator = coneFadingList.iterator(); lightIterator.hasNext();){
			MyConeLight fadingLight = lightIterator.next();

			fadingLight.setDistance(fadingLight.getDistance() - 12 * Gdx.graphics.getDeltaTime());
			if (fadingLight.getDistance() <= 0) {
				fadingLight.setDistance(0);
				myConeLightPool.free(fadingLight);
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

			notesWaiting.add(noteEvent);
	}

	@Override
	public void deviceAdded(String manufacturer) {
		this.manufacturer = manufacturer;
	}
}

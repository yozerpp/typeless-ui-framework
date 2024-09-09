import TypelessHTMLFormatter, {UiMetadata} from "./TypelessHTMLFormatter";
class Client{
  public formatter : TypelessHTMLFormatter | undefined;
  public url: string;
  private metadataPath: string ;
  private async initializeFormatter() : Promise<TypelessHTMLFormatter>{
    return new TypelessHTMLFormatter(Object.entries((await this.fetch<UiMetadata[]>(this.metadataPath)).content));
  }
  public constructor(url:string, metadataPath:string = "metadata"){
    this.url = url;
    this.metadataPath = metadataPath;
  }
  public async getHTML(path:string) : Promise<string>{
    if (this.formatter==undefined) this.formatter= await this.initializeFormatter();
    let root = await this.fetch(path);
    return this.formatter.format(root.rootName, root.content);
  }
  public async getJSON<T=unknown>(path:string):Promise<IRootNameAndContent<T>>{
    return this.fetch<T>(path);
  }
  private async fetch<T=unknown>(path: string) : Promise<IRootNameAndContent<T>>{
    let l= await (await fetch(this.url+ '/' + path)).json();
    const [rootName,json] =<[string,T]>Object.entries(l)[0];
    return {rootName, content: json} ;
  }
}
export interface IRootNameAndContent<T>{
  rootName :string;
  content:T;
}
const serverURL:string ="http://localhost:8080";
const client:Client = new Client(serverURL);
document.addEventListener("DOMContentLoaded", init);
async function init(){
  await loadGeneratedElements();
}
async function loadGeneratedElements(){
  for (const el of document.querySelectorAll(".typeless-data")) {
    const path =el.getAttribute("data-path");
    if (path==undefined) {
      console.log("data-path attribute must point to the url that contains the data that will populate the element.");
      continue;
    }
    el.innerHTML =  await client.getHTML(path);
  }
}

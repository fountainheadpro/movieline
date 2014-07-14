  /** @jsx React.DOM */

  var SearchBox = React.createClass({
    getInitialState: function() {
      this.props.scope.query="Zed's dead...";
      return {
        prompt: "Zed's dead...",
        query: "Zed's dead..."
      };
    },  

    handleChange: function(){
      var _this=this;
      this.state.query=this.refs.quoteTextInput.getDOMNode().value; 
      var scope=this.props.scope
      scope.search(this.state.query);
    }, 

    render: function() {     
        return (
            <form onSubmit={this.handleChange}>
              <div className="searchContainer" >
                <input 
                  type="text" 
                  ng-model="search.query"
                  className="searchBar" 
                  placeholder={this.state.prompt} 
                  ref="quoteTextInput" 
                  //value={this.state.query} 
                  />
                <span className="glyphicon glyphicon-search searchButton"  ></span>
              </div>  
            </form>
        );
    }
  });


var Scene=React.createClass({
  render: function(){
    return (
          <div className="col-sm-6 col-md-6">
            <div className="thumbnail" onClick={this.handleClick}>      
              <img src={this.props.scene.urls[0]} alt={this.props.scene.caption}/>                          
              <blockquote>
                <p>{this.props.scene.caption}</p>
              </blockquote>
            </div>
          </div>                   
    )    
  },

  handleClick: function(event) {
    
  },

});  

/** @jsx React.DOM */
var Clip = React.createClass({
    render: function() {
      ///var scope = this.props.scope;
      var images = [];

      this.props.scope.data.forEach(function(scene){
        images.push(
              <Scene scene={scene} key={scene.id}/>
            )
      }.bind(this));
      return (
          <div className="thumbnails">
            {images}
          </div>
      )      
    }
});